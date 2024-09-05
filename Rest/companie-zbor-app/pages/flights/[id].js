import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import FlightForm from '../../components/FlightForm';

const FlightDetails = () => {
  const router = useRouter();
  const { id } = router.query;
  const [flight, setFlight] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      fetch(`http://localhost:8080/service/flights-request/${id}`)
        .then((response) => response.json())
        .then((data) => {
          setFlight(data);
          setLoading(false);
        })
        .catch((error) => {
          console.error('Error fetching flight:', error);
          setLoading(false);
        });
    }
  }, [id]);

  const handleUpdate = (updatedFlight) => {
    fetch(`http://localhost:8080/service/flights-request/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(updatedFlight),
    })
      .then((response) => response.json())
      .then((data) => {
        setFlight(data);
        alert('Flight updated successfully');
      })
      .catch((error) => console.error('Error updating flight:', error));
  };

  const handleDelete = () => {
    fetch(`http://localhost:8080/service/flights-request/${id}`, {
      method: 'DELETE',
    })
      .then(() => {
        alert('Flight deleted successfully');
        router.push('/flights');
      })
      .catch((error) => console.error('Error deleting flight:', error));
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div>
      <h1>Flight Details</h1>
      {flight ? (
        <div>
          <FlightForm onSubmit={handleUpdate} initialData={flight} submitButtonText="Update" />
          <div style={{ marginTop: '20px' }}>
            <button type="submit" form="flightForm" style={{ marginRight: '10px' }}>
              Update
            </button>
            <button onClick={handleDelete}>Delete</button>
          </div>
        </div>
      ) : (
        <p>Flight not found</p>
      )}
    </div>
  );
};

export default FlightDetails;
