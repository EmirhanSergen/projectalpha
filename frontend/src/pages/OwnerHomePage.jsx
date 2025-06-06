import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Star, Tag, Calendar, MessageCircle, LogOut, ChevronRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import '../styles/OwnerHomePage.css';
import axios from 'axios';
import {getUserIdFromStorage, getUserRoleFromStorage, fetchUserData} from '../services/userService';
import {FaExclamationCircle} from "react-icons/fa";
import {getBusinessReviews} from "../services/businessService.js";

export default function RestaurantOwnerDashboard() {
    const navigate = useNavigate();
    const [restaurantId, setRestaurantId] = useState(null);
    const [restaurantname, setRestaurantName] = useState('');
    const ownerData = JSON.parse(localStorage.getItem('ownerData'));
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isOwner, setIsOwner] = useState(false);
    const [error, setError] = useState(null);
    const [reviews, setReviews] = useState([]);
    const businessData = JSON.parse(localStorage.getItem('ownerData'));
    const token = localStorage.getItem('token');
    const role = getUserRoleFromStorage();


    const handleLoginClick = () => {
        navigate('/');
        // If we're on HomePage, trigger the login popup
        setTimeout(() => {
            const homePageInstance = window.homePageInstance;
            if (homePageInstance && typeof homePageInstance.openLoginPopup === 'function') {
                homePageInstance.openLoginPopup();
            }
        }, 100);
    };
    const handleReturnHomeClick = () => {
        navigate('/');
    }

    useEffect(() => {
        if (!token) {
            setIsAuthenticated(false);
            setError('Bu sayfayı görüntülemek için giriş yapmalısınız.');
            setIsLoading(false);
            return;
        } else if (role !== 'owner_user' && token) {
            setError('Bu sayfa sadece işletme sahipleri içindir.');
            setIsLoading(false);
            setIsOwner(false);
            setIsAuthenticated(true);
            return;
        }
        setIsAuthenticated(true);
        setIsOwner(true);
        setIsLoading(false);
    }, []);

    useEffect(() => {
    const fetchRestaurant = async () => {
        setRestaurantName(businessData.name);
        setRestaurantId(businessData.id);
        try {
            const name =
                businessData.name || 'İşletme Adı';

            setRestaurantName(name);

            const response = await getBusinessReviews(businessData.id);
            setReviews(response);
            console.log(response);
        } catch (err) {
            console.error('İşletme adı alınamadı:', err);
            setRestaurantName('İşletme Adı');
        }
    };

    fetchRestaurant();
}, [businessData]);

    const stats = {
        reviews: reviews.length > 0 ? { count: reviews.length, new: 1 } : { count: 0, new: 0 },
        promotions: { count: 3, active: 2 },
        reservations: { count: 18, today: 6 },
        questions: { count: 8, unanswered: 3 }
    };
    if (!isAuthenticated) {
        return (
            <div className="not-authenticated">
                <div className="auth-error">
                    <FaExclamationCircle className="error-icon" />
                    <h2>Giriş Gerekli</h2>
                    <p>{error || 'Bu sayfayı görüntülemek için giriş yapmalısınız.'}</p>
                    <button className="login-btn" onClick={handleLoginClick}>Giriş Yap</button>
                </div>
            </div>
        );
    }
    if(isAuthenticated && !isOwner) {
        return (
            <div className="not-authenticated">
                <div className="auth-error">
                    <FaExclamationCircle className="error-icon" />
                    <h2>FeastFine</h2>
                    <p>{error || 'Bu sayfayı görüntülemek için işletmeci olmalısınız.'}</p>
                    <button className="login-btn" onClick={handleReturnHomeClick}>Ana sayfaya dön</button>
                </div>
            </div>
        );
    }

    if (error && isAuthenticated) {
        return <div className="error-message">{error}</div>;
    }
    return (

        <div className="dashboard-container">
            <header className="dashboard-header">
                <div className="dashboard-header-content">
                    <div>
                        <h1 className="dashboard-title">{restaurantname}</h1>
                        <p className="dashboard-subtitle">İşletme Hesabı</p>
                    </div>

                </div>
            </header>

            <main className="dashboard-main">
                <div className="dashboard-grid">
                    <DashboardCard
                        title="Reviews"
                        icon={<Star size={24} className="icon-yellow" />}
                        count={stats.reviews.count}
                        subtitle={`${stats.reviews.new} yeni yorum`}
                        cardClass="card-yellow"
                        restaurantId={restaurantId}
                    />
                    <DashboardCard
                        title="Promotions"
                        icon={<Tag size={24} className="icon-blue" />}
                        count={stats.promotions.count}
                        subtitle={`${stats.promotions.active} aktif promosyon`}
                        cardClass="card-blue"
                        restaurantId={restaurantId}
                    />
                    <DashboardCard
                        title="Reservations"
                        icon={<Calendar size={24} className="icon-green" />}
                        count={stats.reservations.count}
                        subtitle={`bugün için ${stats.reservations.today} yeni rezervasyon`}
                        cardClass="card-green"
                        restaurantId={restaurantId}
                    />
                    <DashboardCard
                        title="Customer Questions"
                        icon={<MessageCircle size={24} className="icon-purple" />}
                        count={stats.questions.count}
                        subtitle={`cevaplanmamış ${stats.questions.unanswered} soru`}
                        cardClass="card-purple"
                        restaurantId={restaurantId}
                    />
                </div>
            </main>
        </div>
    );
}

// Her kart bir Link olarak çalışır ve ilgili sayfaya yönlendirir
function DashboardCard({ title, icon, count, subtitle, cardClass, restaurantId }) {
    // Başlık → route segmenti eşleştirmesi
    const routeMap = {
        Reviews: 'reviews',
        Promotions: 'promotions',
        Reservations: 'reservations',
        'Customer Questions': 'questions'
    };

    const path = `/restaurant/${restaurantId}/${routeMap[title]}`;

    return (
        <Link to={path} className={`dashboard-card ${cardClass}`} style={{ textDecoration: 'none' }}>
            <div className="card-content">
                <div className="card-header">
                    <div className="card-title">
                        {icon}
                        <h3>{title}</h3>
                    </div>
                    <ChevronRight className="chevron-icon" />
                </div>
                <div className="card-body">
                    <div className="card-count">{count}</div>
                    <p className="card-subtitle">{subtitle}</p>
                </div>
            </div>
        </Link>
    );
}

