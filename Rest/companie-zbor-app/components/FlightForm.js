import { useState, useEffect } from 'react';

const FlightForm = ({ onSubmit, initialData }) => {
  const [formData, setFormData] = useState({
    id: '',
    destination: '',
    date: '',
    airport: '',
    noTotalSeats: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form id="flightForm" onSubmit={handleSubmit}>
      <div>
        <label>Destination:</label>
        <input type="text" name="destination" value={formData.destination} onChange={handleChange} />
      </div>
      <div>
        <label>Date:</label>
        <input type="date" name="date" value={formData.date} onChange={handleChange} />
      </div>
      <div>
        <label>Airport:</label>
        <input type="text" name="airport" value={formData.airport} onChange={handleChange} />
      </div>
      <div>
        <label>Total Seats:</label>
        <input type="number" name="noTotalSeats" value={formData.noTotalSeats} onChange={handleChange} />
      </div>
      <button type="submit">Add </button>
    </form>
  );
};

export default FlightForm;
