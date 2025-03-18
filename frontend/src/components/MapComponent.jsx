import { MapContainer, TileLayer, Marker, Popup, useMapEvents } from "react-leaflet";
import { useState, useEffect } from "react";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// Duraklar için özel ikon tanımlama
const stopIcon = new L.Icon({
    iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png", // Otobüs/tramvay ikonu
    iconSize: [25, 25],
    iconAnchor: [12, 25],
    popupAnchor: [0, -20],
});

const MapComponent = ({ start, end, onSelectLocation }) => {
    const [clickedPosition, setClickedPosition] = useState(null);
    const [duraklar, setDuraklar] = useState([]); // Durakları saklamak için state

    // JSON'dan durakları çek
    useEffect(() => {
        fetch("/RawData.json") // JSON dosyanızın yolu burada olmalı
            .then((response) => response.json())
            .then((data) => setDuraklar(data.duraklar)) // "duraklar" listesini kaydediyoruz
            .catch((error) => console.error("Durakları yüklerken hata oluştu:", error));
    }, []);

    function LocationMarker() {
        useMapEvents({
            click(e) {
                setClickedPosition(e.latlng);
                onSelectLocation(e.latlng); // Seçilen konumu RouteFinder’a gönder
            },
        });

        return clickedPosition ? (
            <Marker position={clickedPosition}>
                <Popup>Seçilen Konum</Popup>
            </Marker>
        ) : null;
    }

    return (
        <MapContainer center={[40.7696, 29.9406]} zoom={13} style={{ height: "400px", width: "100%" }}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

            {/* Başlangıç ve Hedef Noktaları */}
            {start && (
                <Marker position={[start.lat, start.lon]}>
                    <Popup>Başlangıç Noktası</Popup>
                </Marker>
            )}
            {end && (
                <Marker position={[end.lat, end.lon]}>
                    <Popup>Hedef Noktası</Popup>
                </Marker>
            )}

            {/* Durakları haritaya ekle */}
            {duraklar.map((durak) => (
                <Marker key={durak.id} position={[durak.lat, durak.lon]} icon={stopIcon}>
                    <Popup>
                        <b>{durak.name}</b> <br />
                        Tür: {durak.type === "bus" ? "Otobüs" : "Tramvay"}
                    </Popup>
                </Marker>
            ))}

            <LocationMarker />
        </MapContainer>
    );
};

export default MapComponent;