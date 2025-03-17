import React, { useState } from "react";
import MapComponent from "./components/MapComponent";
import RouteForm from "./components/RouteForm";
import RouteResults from "./components/RouteResults";
import axios from "axios";

const App = () => {
    const [results, setResults] = useState({});

    const handleCalculateRoute = (formData) => {
        axios.post("http://localhost:8080/calculateRoute", formData)
            .then(response => setResults(response.data))
            .catch(error => console.error("API hatası:", error));
    };

    return (
        <div className="flex">
            <MapComponent stops={[]} onMapClick={() => {}} />
            <div className="p-4 w-1/3">
                <RouteForm onSubmit={handleCalculateRoute} />
                <RouteResults results={results} />
            </div>
        </div>
    );
};

export default App;
