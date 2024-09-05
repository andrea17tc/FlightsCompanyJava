import FlightItem from './FlightItem';

const FlightList = ({ flights, onDelete }) => {
  return (
    <div>
      {flights.map(flight => (
        <FlightItem key={flight.id} flight={flight} onDelete={onDelete} />
      ))}
    </div>
  );
};

export default FlightList;
