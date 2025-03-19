import { MapContainer, TileLayer, Marker, Popup, useMapEvents, Polyline } from "react-leaflet";
import { useState, useEffect } from "react";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

// Duraklar için özel ikon tanımlama
const stopIcon = new L.Icon({
    iconUrl: "https://cdn-icons-png.flaticon.com/512/684/684908.png",
    iconSize: [25, 25],
    iconAnchor: [12, 25],
    popupAnchor: [0, -20],
});

const MapComponent = ({
                          start, end, onSelectLocation,
                          routeCoordinates, nearestStartStop, nearestEndStop
                      }) => {
    const [clickedPosition, setClickedPosition] = useState(null);
    const [duraklar, setDuraklar] = useState([]);


    useEffect(() => {
        fetch("/RawData.json")
            .then((response) => response.json())
            .then((data) => setDuraklar(data.duraklar))
            .catch((error) => console.error("Durakları yüklerken hata oluştu:", error));
    }, []);

    function LocationMarker() {
        useMapEvents({
            click(e) {
                setClickedPosition(e.latlng);
                onSelectLocation(e.latlng);
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

            {duraklar.map((durak) => (
                <Marker key={durak.id} position={[durak.lat, durak.lon]} icon={stopIcon}>
                    <Popup>
                        <b>{durak.name}</b> <br />
                        Tür: {durak.type === "bus" ? "Otobüs" : "Tramvay"}
                    </Popup>
                </Marker>
            ))}

            {routeCoordinates && routeCoordinates.length > 0 && (
                <Polyline positions={routeCoordinates} pathOptions={{ color: 'black', weight: 5 }} />
            )}
            {start && nearestStartStop && (
                <Polyline positions={[
                    [start.lat, start.lon],
                    [nearestStartStop.lat, nearestStartStop.lon]
                ]} pathOptions={{color: 'black', dashArray: '5, 10'}} />
            )}

            {end && nearestEndStop && (
                <Polyline positions={[
                    [end.lat, end.lon],
                    [nearestEndStop.lat, nearestEndStop.lon]
                ]} pathOptions={{color: 'black', dashArray: '5, 10'}} />
            )}




            <LocationMarker />
        </MapContainer>
    );
};

export default MapComponent;
