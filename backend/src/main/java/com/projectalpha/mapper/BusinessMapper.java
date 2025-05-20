package com.projectalpha.mapper;

import com.projectalpha.dto.business.address.AddressDto;
import com.projectalpha.dto.business.BusinessDto;
import com.projectalpha.dto.business.OperatingHourDto;
import com.projectalpha.dto.business.photo.PhotoDto;
import com.projectalpha.dto.business.settings.RestaurantSettingsDto;
import com.projectalpha.dto.business.tag.BusinessTagDto;
import com.projectalpha.model.business.Business;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * İşletme varlıklarını DTO'lara ve DTO'ları işletme varlıklarına dönüştüren mapper sınıfı
 */
@Component
public class BusinessMapper {

    /**
     * İşletme varlığını DTO'ya dönüştürür
     *
     * @param business İşletme varlığı
     * @return İşletme DTO
     */
    public BusinessDto toDto(Business business) {
        if (business == null) {
            return null;
        }

        BusinessDto dto = new BusinessDto();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setDescription(business.getDescription());

        // Kategori bilgisi BusinessTag'ler arasından alınabilir veya farklı bir kaynak gerekmektedir
        // dto.setCategory(business.getCategory());

        // Adres bilgisi için AddressDto oluşturun
        if (business.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            // Address nesnesinden gerekli bilgileri alarak AddressDto'yu doldurun
            dto.setAddress(addressDto);
        }

        // İletişim bilgileri - bunlar için Business sınıfında ilgili alanlar var mı kontrol edilmeli
        // dto.setPhoneNumber(business.getPhoneNumber());
        // dto.setEmail(business.getEmail());
        // dto.setWebsite(business.getWebsite());

        // İşletme saatleri
        if (business.getOperatingHours() != null) {
            List<OperatingHourDto> operatingHourDtos = new ArrayList<>();
            // OperatingHour listesinden OperatingHourDto listesi oluşturun
            dto.setOperatingHours(operatingHourDtos);
        }

        // Fotoğraflar
        if (business.getPhotos() != null) {
            List<PhotoDto> photoDtos = new ArrayList<>();
            // Photo listesinden PhotoDto listesi oluşturun
            dto.setPhotos(photoDtos);
        }

        // Restoran ayarları
        if (business.getSettings() != null) {
            RestaurantSettingsDto settingsDto = new RestaurantSettingsDto();
            // RestaurantSettings nesnesinden RestaurantSettingsDto oluşturun
            dto.setSettings(settingsDto);
        }

        // Etiketler
        if (business.getBusinessTags() != null) {
            List<BusinessTagDto> tagDtos = new ArrayList<>();
            // BusinessTag listesinden BusinessTagDto listesi oluşturun
            dto.setTags(tagDtos);
        }

        // Derecelendirme bilgileri
        dto.setAverageRating(business.getAvgRating() != null ?
                business.getAvgRating().doubleValue() : null);

        // İnceleme sayısı - reviews listesinin boyutu kullanılabilir
        dto.setReviewCount(business.getReviews() != null ?
                business.getReviews().size() : null);

        return dto;
    }

    /**
     * İşletme DTO'sunu varlığa dönüştürür
     *
     * @param businessDto İşletme DTO
     * @return İşletme varlığı
     */
    public Business toEntity(BusinessDto businessDto) {
        if (businessDto == null) {
            return null;
        }

        Business business = new Business();
        business.setId(businessDto.getId());
        business.setName(businessDto.getName());
        business.setDescription(businessDto.getDescription());

        // İlişkili nesnelerin (Address, OperatingHours vb.) oluşturulması ve
        // set edilmesi ayrı bir helper metot ile yapılabilir

        return business;
    }

    /**
     * İşletme varlıklarının listesini DTO listesine dönüştürür
     *
     * @param businesses İşletme varlıkları listesi
     * @return İşletme DTO'ları listesi
     */
    public List<BusinessDto> toDtoList(List<Business> businesses) {
        if (businesses == null) {
            return null;
        }

        return businesses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * İşletme DTO'larının listesini varlık listesine dönüştürür
     *
     * @param businessDtos İşletme DTO'ları listesi
     * @return İşletme varlıkları listesi
     */
    public List<Business> toEntityList(List<BusinessDto> businessDtos) {
        if (businessDtos == null) {
            return null;
        }

        return businessDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Mevcut işletmeyi DTO'dan gelen bilgilerle günceller
     *
     * @param existingBusiness Mevcut işletme
     * @param businessDto Güncellenecek DTO
     */
    public void updateEntityFromDto(Business existingBusiness, BusinessDto businessDto) {
        if (existingBusiness == null || businessDto == null) {
            return;
        }

        if (businessDto.getName() != null) {
            existingBusiness.setName(businessDto.getName());
        }

        if (businessDto.getDescription() != null) {
            existingBusiness.setDescription(businessDto.getDescription());
        }

        // İlişkili nesnelerin (Address, OperatingHours vb.) güncellenmesi
        // ayrı bir helper metot ile yapılabilir
    }
}