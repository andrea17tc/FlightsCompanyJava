import { useRouter } from 'next/router';
import FlightForm from '../../components/FlightForm';

const AddFlight = () => {
  const router = useRouter();

  const addFlight = async (flight) => {
    const response = await fetch('http://localhost:8080/service/flights-request', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(flight),
    });
    if (response.ok) {
      router.push('/flights');
    }
  };

  return <FlightForm onSubmit={addFlight} />;
};

export default AddFlight;
