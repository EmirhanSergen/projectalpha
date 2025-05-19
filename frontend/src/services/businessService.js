import axios from 'axios';

// Varsayılan fotoğraflar için Public URL'ler (doğrudan erişilebilir)
const DEFAULT_IMAGES = [
    'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80', // Restaurant
    'https://images.unsplash.com/photo-1554118811-1e0d58224f24?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80', // Cafe
    'https://images.unsplash.com/photo-1514933651103-005eec06c04b?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80', // Bar
    'https://images.unsplash.com/photo-1559925393-8be0ec4767c8?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80', // Food
    'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?ixlib=rb-4.0.3&auto=format&fit=crop&w=600&q=80'  // Restaurant interior
];


const SUPABASE_URL = 'https://jfqmvaeeelneeawbvkga.supabase.co';
const SUPABASE_ANON_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpmcW12YWVlZWxuZWVhd2J2a2dhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU3MDAwNzgsImV4cCI6MjA2MTI3NjA3OH0.B9R9mUucwZCYXSuZPD3PKGcNUfR58Zb4Efb0kyUe2RM';

const supabaseClient = axios.create({
    baseURL: `${SUPABASE_URL}/rest/v1`,
    headers: {
        'apikey': SUPABASE_ANON_KEY,
        'Authorization': `Bearer ${SUPABASE_ANON_KEY}`,
        'Content-Type': 'application/json',
        'Prefer': 'return=representation'
    }
});

// İşletmeleri getir
export const getBusinesses = async () => {
    try {
        const response = await supabaseClient.get('/business?select=*');
        // Varsayılan fotoğraflar ekle
        return addDefaultImages(response.data);
    } catch (error) {
        console.error('İşletmeler getirilirken hata oluştu:', error);
        throw error;
    }
};

// Öne çıkan işletmeleri getir (örnek olarak)
export const getFeaturedBusinesses = async () => {
    try {
        // Burada featured=true gibi bir filtre ekleyebilirsiniz
        // Eğer business tablosunda featured kolonu yoksa, eklememiz gerekecek
        const response = await supabaseClient.get('/business?select=*&limit=6');
        // Varsayılan fotoğraflar ekle
        return addDefaultImages(response.data);
    } catch (error) {
        console.error('Öne çıkan işletmeler getirilirken hata oluştu:', error);
        throw error;
    }
};

// Favori işletmeleri getir
export const getFavorites = async () => {
    try {
        // Burada kullanıcının favori işletmelerini getiren kodu yazın
        // Örnek olarak:
        const response = await supabaseClient.get('/favorites?select=*');
        return response.data;
    } catch (error) {
        console.error('Favoriler getirilirken hata oluştu:', error);
        throw error;
    }
};

// İşletme ara
export const searchBusinesses = async (searchTerm) => {
    try {
        // İsme göre filtreleme
        const response = await supabaseClient.get(`/business?select=*&name=ilike.%${searchTerm}%`);
        // Varsayılan fotoğraflar ekle
        return addDefaultImages(response.data);
    } catch (error) {
        console.error('İşletme aranırken hata oluştu:', error);
        throw error;
    }
};


// Favorilere ekle/çıkar
export const toggleFavorite = async (businessId, isFavorite) => {
    try {
        if (isFavorite) {
            // Favorilere ekle
            const response = await supabaseClient.post('/favorites', {
                business_id: businessId,
                // Eğer kullanıcı kimliği gerekiyorsa, bu kısmı düzenlemeniz gerekebilir
                user_id: 'current-user-id' // Gerçek uygulamada oturum açmış kullanıcının ID'si
            });
            return response.data;
        } else {
            // Favorilerden çıkar
            const response = await supabaseClient.delete('/favorites', {
                params: {
                    business_id: `eq.${businessId}`,
                    // Eğer kullanıcı kimliği gerekiyorsa, bu kısmı düzenlemeniz gerekebilir
                    user_id: `eq.current-user-id` // Gerçek uygulamada oturum açmış kullanıcının ID'si
                }
            });
            return response.data;
        }
    } catch (error) {
        console.error('Favori durumu değiştirilirken hata oluştu:', error);
        throw error;
    }
};


// İşletme detayı getir
export const getBusinessById = async (id) => {
    try {
        const response = await supabaseClient.get(`/business?id=eq.${id}&select=*`);
        // Tek bir işletme döndürüyorsak varsayılan fotoğraf ekleyelim
        if (response.data && response.data.length > 0) {
            return addDefaultImage(response.data[0]);
        }
        return null;
    } catch (error) {
        console.error(`ID'si ${id} olan işletme getirilirken hata oluştu:`, error);
        throw error;
    }
};

// Varsayılan fotoğrafları eklemek için yardımcı fonksiyon
const addDefaultImages = (businesses) => {
    if (!businesses) return [];
    return businesses.map((business, index) => {
        if (!business.image) {
            return {
                ...business,
                image: DEFAULT_IMAGES[index % DEFAULT_IMAGES.length]
            };
        }
        return business;
    });
};

// Tek bir işletmeye varsayılan fotoğraf ekleyen yardımcı fonksiyon
const addDefaultImage = (business) => {
    if (!business) return business;

    // Her işletme için varsayılan bir fotoğraf ata
    const imageIndex = business.id % DEFAULT_IMAGES.length;

    // Orijinal görüntüyü koruyalım, ancak varsayılan görüntüyü de ekleyelim
    return {
        ...business,
        image: DEFAULT_IMAGES[imageIndex] // Her durumda varsayılan görüntüyü kullanalım
    };
};

