import { useState } from "react";
import MapComponent from "./MapComponent";

export default function RouteFinder() {
    const [passengerType, setPassengerType] = useState("ogrenci");
    const [paymentMethod, setPaymentMethod] = useState("kentkart");
    const [startLat, setStartLat] = useState("");
    const [startLon, setStartLon] = useState("");
    const [endLat, setEndLat] = useState("");
    const [endLon, setEndLon] = useState("");
    const [routes, setRoutes] = useState([]);
    const [isSelectingStart, setIsSelectingStart] = useState(true);
    const [selectedRouteCoords, setSelectedRouteCoords] = useState([]);

    // yeni eklediğiniz stateler de burada olacak:
    const [nearestStartStopName, setNearestStartStopName] = useState("");
    const [nearestEndStopName, setNearestEndStopName] = useState("");
    const [distanceToStartStop, setDistanceToStartStop] = useState(0);
    const [distanceToEndStop, setDistanceToEndStop] = useState(0);
    const [startMessage, setStartMessage] = useState("");
    const [endMessage, setEndMessage] = useState("");
    const [nearestStartStop, setNearestStartStop] = useState(null);
    const [nearestEndStop, setNearestEndStop] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const requestData = {
            passengerType,
            paymentMethod,
            start: { lat: parseFloat(startLat), lon: parseFloat(startLon) },
            end: { lat: parseFloat(endLat), lon: parseFloat(endLon) }
        };

        try {
            const response = await fetch("http://localhost:8080/api/routes", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestData)
            });
            const data = await response.json();
            setRoutes(data.routes);
            setNearestStartStopName(data.nearestStartStopName);
            setDistanceToStartStop(data.distanceToStartStop);
            setStartMessage(data.startMessage);
            setNearestEndStopName(data.nearestEndStopName);
            setDistanceToEndStop(data.distanceToEndStop);
            setEndMessage(data.endMessage);

            setNearestStartStop(data.routes[1].coordinates[0]);
            setNearestEndStop(data.routes[1].coordinates[data.routes[1].coordinates.length - 1]);
        } catch (error) {
            console.error("Hata:", error);
        }
    };

    const onMapClick = (latlng) => {
        if (isSelectingStart) {
            setStartLat(latlng.lat);
            setStartLon(latlng.lng);
        } else {
            setEndLat(latlng.lat);
            setEndLon(latlng.lng);
        }
    };

    return (
        <div className="container">
            <h2>Rota Bulucu</h2>
            <form onSubmit={handleSubmit}>
                <label>Yolcu Tipi:</label>
                <select value={passengerType} onChange={(e) => setPassengerType(e.target.value)}>
                    <option value="ogrenci">Öğrenci</option>
                    <option value="yasli">Yaşlı</option>
                    <option value="yetiskin">Yetişkin</option>
                </select>

                <label>Ödeme Yöntemi:</label>
                <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
                    <option value="kentkart">KentKart</option>
                    <option value="kredikart">Kredi Kartı</option>
                    <option value="nakit">Nakit</option>
                </select>

                <label>Başlangıç Konumu (Lat, Lon):</label>
                <input type="text" placeholder="Lat" value={startLat} readOnly />
                <input type="text" placeholder="Lon" value={startLon} readOnly />

                <label>Varış Konumu (Lat, Lon):</label>
                <input type="text" placeholder="Lat" value={endLat} readOnly />
                <input type="text" placeholder="Lon" value={endLon} readOnly />

                <button type="submit">Rota Hesapla</button>
            </form>

            <button onClick={() => setIsSelectingStart(true)}>Başlangıç Seç</button>
            <button onClick={() => setIsSelectingStart(false)}>Hedef Seç</button>

            <MapComponent
                start={{ lat: startLat, lon: startLon }}
                end={{ lat: endLat, lon: endLon }}
                onSelectLocation={onMapClick}
                routeCoordinates={selectedRouteCoords}
                nearestStartStop={nearestStartStop}
                nearestEndStop={nearestEndStop}
            />

            <div>
                <p><strong>Başlangıç için en yakın durak:</strong> {nearestStartStopName} ({distanceToStartStop.toFixed(2)} km)</p>
                <p>{startMessage}</p>

                <p><strong>Hedef için en yakın durak:</strong> {nearestEndStopName} ({distanceToEndStop.toFixed(2)} km)</p>
                <p>{endMessage}</p>
            </div>

            <h3>Alternatif Rotalar</h3>
            {routes.length > 0 ? (
                <ul>
                    {routes.map((route, index) => (
                        <li
                            key={index}
                            style={{ cursor: "pointer" }}
                            onClick={() => {
                                if (route.coordinates && route.coordinates.length > 0) {
                                    const coords = route.coordinates.map(coord => [coord.lat, coord.lon]);
                                    setSelectedRouteCoords(coords);
                                } else {
                                    setSelectedRouteCoords([]);
                                }
                            }}
                        >
                            <strong>{route.type === "Taksi" ? "🚖 Taksi" : route.type}</strong>: {route.path.join(" → ")} |
                            Ücret: {route.fare.toFixed(2)} TL | Süre: {route.duration} dk | Aktarma: {route.transfers}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Henüz bir rota hesaplanmadı.</p>
            )}
        </div>
    );
}
