import React from "react";

const RouteResults = ({ results }) => {
    return (
        <div className="p-4 bg-gray-200">
            <h2 className="text-lg font-bold mb-2">Hesaplanan Rotalar</h2>
            <ul>
                <li><strong>En Ucuz Rota:</strong> {results.enUcuz}</li>
                <li><strong>En Kısa Rota:</strong> {results.enKisa}</li>
                <li><strong>En Az Aktarmalı Rota:</strong> {results.enAzAktarma}</li>
                <li><strong>En Hızlı Rota (Taksi):</strong> {results.enHizli}</li>
            </ul>
        </div>
    );
};

export default RouteResults;
