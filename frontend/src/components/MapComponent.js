import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// İkonlar
const busIcon = new L.Icon({
    iconUrl: "https://cdn-icons-png.flaticon.com/512/2795/2795112.png",
    iconSize: [35, 35],
    iconAnchor: [17, 34],
    popupAnchor: [0, -30]
});

const tramIcon = new L.Icon({
    iconUrl: "https://cdn-icons-png.flaticon.com/512/2344/2344092.png",
    iconSize: [35, 35],
    iconAnchor: [17, 34],
    popupAnchor: [0, -30]
});

// Haversine Mesafe Hesaplama (km)
const haversineDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371; // Dünya yarıçapı (km)
    const dLat = (lat2 - lat1) * (Math.PI / 180);
    const dLon = (lon2 - lon1) * (Math.PI / 180);
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1 * (Math.PI / 180)) * Math.cos(lat2 * (Math.PI / 180)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
};

const MapComponent = () => {
    const [stops, setStops] = useState([]);
    const [userLocation, setUserLocation] = useState({ lat: "", lon: "" });
    const [nearestStop, setNearestStop] = useState(null);

    // JSON'dan durakları al
    useEffect(() => {
        fetch(process.env.PUBLIC_URL + "/RawData.json")
            .then(response => response.json())
            .then(data => {
                if (data && data.stops && Array.isArray(data.stops)) {
                    setStops(data.stops);
                } else {
                    console.error("Hata: JSON dosyasında 'stops' dizisi bulunamadı.");
                }
            })
            .catch(error => console.error("Durakları yüklerken hata oluştu:", error));
    }, []);

    // Kullanıcının en yakın durağını bul
    const findNearestStop = () => {
        if (!userLocation.lat || !userLocation.lon) return;

        let minDistance = Infinity;
        let closestStop = null;

        stops.forEach(stop => {
            const distance = haversineDistance(userLocation.lat, userLocation.lon, stop.lat, stop.lon);
            if (distance < minDistance) {
                minDistance = distance;
                closestStop = stop;
            }
        });

        setNearestStop(closestStop);
    };

    return (
        <div>
            {/* Kullanıcıdan Manuel Konum Girişi */}
            <div style={{ padding: "10px" }}>
                <h3>📍 Konum Seç</h3>
                <label>Enlem (Lat):</label>
                <input
                    type="number"
                    value={userLocation.lat}
                    onChange={(e) => setUserLocation({ ...userLocation, lat: parseFloat(e.target.value) })}
                />
                <label>Boylam (Lon):</label>
                <input
                    type="number"
                    value={userLocation.lon}
                    onChange={(e) => setUserLocation({ ...userLocation, lon: parseFloat(e.target.value) })}
                />
                <button onClick={findNearestStop}>En Yakın Durağı Bul</button>
            </div>

            {/* Harita */}
            <MapContainer center={[40.765, 29.94]} zoom={12} style={{ height: "80vh", width: "100%" }}>
                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                {/* Durakları İşaretle */}
                {stops.map((stop, index) => (
                    <Marker
                        key={index}
                        position={[stop.lat, stop.lon]}
                        icon={stop.type === "bus" ? busIcon : tramIcon}
                    >
                        <Popup>{stop.name} ({stop.type})</Popup>
                    </Marker>
                ))}

                {/* Kullanıcının En Yakın Durağı */}
                {nearestStop && (
                    <Marker position={[nearestStop.lat, nearestStop.lon]} icon={busIcon}>
                        <Popup>En Yakın Durak: {nearestStop.name}</Popup>
                    </Marker>
                )}
            </MapContainer>
        </div>
    );
};

export default MapComponent;
