import { useState, useEffect } from 'react';
import Link from 'next/link';
import FlightForm from '../../components/FlightForm';

const HomePage = () => {
  const [flights, setFlights] = useState([]);

  useEffect(() => {
    fetchFlights();
  }, []);

  const fetchFlights = async () => {
    const response = await fetch('http://localhost:8080/service/flights-request');
    const data = await response.json();
    setFlights(data);
  };

  const handleCreateFlight = async (flight) => {
    const response = await fetch('http://localhost:8080/service/flights-request', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(flight),
    });
    const newFlight = await response.json();
    setFlights((prevFlights) => [...prevFlights, newFlight]);
  };

  return (
    <div>
      <h1>Flight Company</h1>
      <h2>All Flights</h2>
      <ul>
        {flights.map((flight) => (
          <li key={flight.id}>
            <Link href={`/flights/${flight.id}`}>
              <p>{flight.destination} ({flight.date})</p>
            </Link>
          </li>
        ))}
      </ul>
      {/* <h2>Add a New Flight</h2>
      <FlightForm onSubmit={handleCreateFlight} /> */}
      <h2>Actions</h2>
      <div>
        <Link href="/search-flight">
          <button>Find a Flight</button>
        </Link>
        <Link href="/flights/add">
          <button>Add a Flight</button>
        </Link>
      </div>
    </div>
  );
};

export default HomePage;
