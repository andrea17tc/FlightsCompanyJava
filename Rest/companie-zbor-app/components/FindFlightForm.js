import { useState } from 'react';

const FindFlightForm = () => {
  const [flightId, setFlightId] = useState('');
  const [flight, setFlight] = useState(null);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setFlightId(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`http://localhost:8080/service/flights-request/${flightId}`);
      if (!response.ok) {
        throw new Error('Flight not found');
      }
      const data = await response.json();
      setFlight(data);
      setError(null);
    } catch (err) {
      setError(err.message);
      setFlight(null);
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter flight ID"
          value={flightId}
          onChange={handleChange}
          required
        />
        <button type="submit">Find</button>
      </form>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {flight && (
        <div>
          <h3>Flight Details</h3>
          <p>ID: {flight.id}</p>
          <p>Destination: {flight.destination}</p>
          <p>Date: {flight.date}</p>
          <p>Airport: {flight.airport}</p>
          <p>Total Seats: {flight.noTotalSeats}</p>
        </div>
      )}
    </div>
  );
};

export default FindFlightForm;
