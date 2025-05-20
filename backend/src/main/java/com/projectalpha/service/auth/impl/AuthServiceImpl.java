package com.projectalpha.service.auth.impl;

import com.projectalpha.dto.auth.request.AuthRequestDto;
import com.projectalpha.dto.thirdparty.SupabaseTokenResponse;
import com.projectalpha.exception.auth.DuplicateEmailException;
import com.projectalpha.exception.auth.UserNotFoundException;
import com.projectalpha.exception.auth.UserNotVerifiedException;
import com.projectalpha.exception.auth.WrongRoleLoginMethod;
import com.projectalpha.model.business.Business;
import com.projectalpha.model.business.address.Address;
import com.projectalpha.repository.auth.AuthRepository;
import com.projectalpha.repository.user.UserProfileDinerRepository;
import com.projectalpha.repository.user.UserProfileOwnerRepository;
import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.dto.business.address.AddressDto;
import com.projectalpha.dto.user.owner.request.OwnerRegistrationRequest;
import com.projectalpha.dto.user.owner.profile.OwnerUserProfile;
// veya doğru paket yollarını kontrol edin
import com.projectalpha.service.auth.AuthService;
import com.projectalpha.service.auth.impl.AuthServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling auth operations.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final UserProfileDinerRepository userRepository;
    private final UserProfileOwnerRepository ownerRepository;

    @Autowired
    public AuthServiceImpl(AuthRepository authRepository, UserProfileDinerRepository userRepository, UserProfileOwnerRepository ownerRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }



    /**
     * Sends a verification code to the specified email for registration.
     *
     * @param email The email to send the verification code to
     * @throws DuplicateEmailException If the email is already registered
     * @throws UserNotVerifiedException If the email is not verified
     * @throws Exception If an error occurs during the process
     */
    @Override
    public void sendVerificationCode(String email) throws Exception {
        // Kullanıcının email'inin veritabanında olup olmadığını kontrol et
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Bu email adresi zaten kayıtlı");
        }

        // Supabase veya benzeri bir servis üzerinden doğrulama kodu gönder
        authRepository.sendVerificationCode(email);
    }

    /**
     * Verifies a token for the specified email.
     *
     * @param email The email associated with the token
     * @param token The verification token
     * @return SupabaseTokenResponse containing access token and user info
     * @throws Exception If verification fails
     */
    @Override
    public SupabaseTokenResponse verifyVerificationCode(String email, String token) throws Exception {
        // Token'ı doğrula
        SupabaseTokenResponse response = authRepository.verifyToken(email, token);

        if (response == null) {
            throw new Exception("Doğrulama kodu geçersiz");
        }

        return response;
    }

    /**
     * Updates a diner's password and role.
     *
     * @param email The user's email
     * @param newPassword The new password to set
     * @param role The role to assign to the user
     * @throws Exception If the update fails
     */
    @Override
    public void updateUser(String email, String newPassword, String role) throws Exception {
        // Kullanıcıyı email ile bul
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        // Şifre güncelleme
        if (newPassword != null && !newPassword.isEmpty()) {
            authRepository.updatePassword(email, newPassword);
        }

        // Rol güncelleme
        if (role != null && !role.isEmpty()) {
            user.setRole(role);
            userRepository.save(user);
        }
    }

    /**
     * Updates a owner's password and role.
     *
     * @param email The user's email
     * @param newPassword The new password to set
     * @param role The role to assign to the user
     * @param request The request came from front-end
     * @throws Exception If the update fails
     */
    @Override
    @Transactional
    public void updateUser(String email, String newPassword, String role, OwnerRegistrationRequest request) throws Exception {
        // Temel şifre/rol güncellemesini yap
        updateUser(email, newPassword, role);

        // Kullanıcıyı bul
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        // Owner profili oluştur veya güncelle
        var ownerProfile = ownerRepository.findById(user.getId())
                .orElse(new UserProfileOwner());

        // User objesini bağla
        ownerProfile.setUser(user);

        // Owner bilgilerini DTO’dan güncelle
        ownerProfile.setUsername(request.getRequestOwner().getUsername());
        ownerProfile.setEmail(request.getRequestOwner().getEmail());
        ownerProfile.setFirstName(request.getRequestOwner().getName());
        ownerProfile.setLastName(request.getRequestOwner().getSurname());
        ownerProfile.setPhoneNumber(request.getRequestOwner().getPhone_numb());
        ownerProfile.setCompanyName(request.getRequestOwner().getCompanyName());
        ownerProfile.setTaxId(request.getRequestOwner().getTaxId());

        // Business bilgisi varsa yeni bir Business entity oluştur
        if (request.getBusiness() != null) {
            BusinessDto businessDto = request.getBusiness();

            Business business = new Business();
            business.setName(businessDto.getName());
            business.setOwner(ownerProfile); // ManyToOne ilişkisi varsa

            // Address varsa bağla
            if (businessDto.getAddress() != null) {
                AddressDto addressDto = businessDto.getAddress();

                Address address = new Address();
                address.setStreetAddress(addressDto.getStreet());
                address.setCity(addressDto.getCity());
                address.setState(addressDto.getState());
                address.setPostalCode(addressDto.getZipCode());
                address.setCountry(addressDto.getCountry());
                address.setLatitude(addressDto.getLatitude());
                address.setLongitude(addressDto.getLongitude());
                address.setBusiness(business); // ters bağlantı

                business.setAddress(address);
            }

            ownerProfile.getBusinesses().add(business);
        }

        // Kaydet
        ownerRepository.save(ownerProfile);
    }


    /**
     * Authenticates a user with email and password.
     *
     * @param email The user's email
     * @param password The user's password
     * @param expectedRole The expected role of the user
     * @return SupabaseTokenResponse containing access token, refresh token, and user info
     * @throws Exception If auth fails
     */
    @Override
    public SupabaseTokenResponse loginWithEmailPassword(String email, String password, String expectedRole) throws Exception {
        // Kullanıcı kimlik doğrulama
        SupabaseTokenResponse response = authRepository.signIn(AuthRequestDto);

        if (response == null) {
            throw new Exception("Giriş başarısız. Email veya şifre hatalı.");
        }

        // Kullanıcının rolünü kontrol et
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı"));

        // Beklenen rolü kontrol et
        if (expectedRole != null ) {
            throw new WrongRoleLoginMethod("Bu giriş yöntemi sizin rolünüz için uygun değil.");
        }

        return response;
    }
}