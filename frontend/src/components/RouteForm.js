import React, { useState } from "react";

const RouteForm = ({ onSubmit }) => {
    const [startLat, setStartLat] = useState("");
    const [startLon, setStartLon] = useState("");
    const [endLat, setEndLat] = useState("");
    const [endLon, setEndLon] = useState("");
    const [yolcuTipi, setYolcuTipi] = useState("ogrenci");

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit({ startLat, startLon, endLat, endLon, yolcuTipi });
    };

    return (
        <div className="p-4 bg-gray-100">
            <h2 className="text-lg font-bold mb-2">Rota Hesaplama</h2>
            <form onSubmit={handleSubmit}>
                <label>Başlangıç Konumu:</label>
                <input type="text" placeholder="Enlem" value={startLat} onChange={(e) => setStartLat(e.target.value)} />
                <input type="text" placeholder="Boylam" value={startLon} onChange={(e) => setStartLon(e.target.value)} />

                <label>Hedef Konumu:</label>
                <input type="text" placeholder="Enlem" value={endLat} onChange={(e) => setEndLat(e.target.value)} />
                <input type="text" placeholder="Boylam" value={endLon} onChange={(e) => setEndLon(e.target.value)} />

                <label>Yolcu Tipi:</label>
                <select value={yolcuTipi} onChange={(e) => setYolcuTipi(e.target.value)}>
                    <option value="ogrenci">Öğrenci</option>
                    <option value="yetiskin">Yetişkin</option>
                    <option value="yasli">Yaşlı</option>
                </select>

                <button type="submit" className="mt-2 bg-blue-500 text-white px-4 py-2">Rota Hesapla</button>
            </form>
        </div>
    );
};

export default RouteForm;
