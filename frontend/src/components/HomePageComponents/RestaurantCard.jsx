import React from 'react';
import { Link } from 'react-router-dom';
import { FaStar, FaMapMarkerAlt, FaHeart, FaRegHeart, FaTag } from 'react-icons/fa';
import './RestaurantCard.css';

/**
 * Restaurant Card component for displaying restaurant information
 * @param {Object} restaurant - Restaurant data
 * @param {Array} favorites - List of favorite restaurant IDs
 * @param {Function} toggleFavorite - Function to toggle favorite status
 * @param {boolean} featured - Whether this is a featured restaurant
 */
const RestaurantCard = ({ restaurant, favorites, toggleFavorite, featured = false }) => {

  const { id, name, type, distance, rating, image, priceRange, hasActivePromo, promoDetails , address , tags } = restaurant;
  const isFavorite = favorites.some(fav => fav.id === id);
  const token = localStorage.getItem('token');
  const isLogin = token !== null;


  return (
    <div className={`restaurant-card ${featured ? 'featured' : ''}`}>
      <Link to={`/restaurant/${id}`} className="restaurant-link">
        <div className="restaurant-image">
          <img src={image} alt={name} />
          <button 
            className="favorite-button" 
            onClick={(e) => toggleFavorite(id, e)}
            aria-label={isFavorite ? "Remove from favorites" : "Add to favorites"}
            disabled={!isLogin}
          >
            {isFavorite ? <FaHeart className="heart filled" /> : <FaRegHeart className="heart" />}
          </button>
          
          {hasActivePromo && (
            <div className="promo-badge">
              <FaTag /> Promo
            </div>
          )}
        </div>
        
        <div className="restaurant-info">
          <div className="restaurant-header">
            <h3>{name}</h3>
            {priceRange && <span className="price-range">{priceRange}</span>}
          </div>

          {/* Mutfak Türü */}
          <p className="restaurant-type">{type}</p>

          {address && (
              <p className="restaurant-location">
                <FaMapMarkerAlt /> {address.city}, {address.district}
              </p>
          )}

          {tags && tags.length > 0 && (
              <div className="tag-list">
                {tags.map(tag => (
                    <span key={tag.id} className="tag">
                  {tag.name.trim()}
                </span>
                ))}
              </div>
          )}
          
          <div className="rating">
            <FaStar className="star" />
            <span>{rating}</span>
          </div>
          
          {hasActivePromo && promoDetails && (
            <div className="promo-details">
              <FaTag className="promo-icon" />
              <span>{promoDetails}</span>
            </div>
          )}
        </div>
      </Link>
    </div>
  );
};

export default RestaurantCard; 