const FlightItem = ({ flight, onDelete }) => {
    const handleDelete = () => {
      onDelete(flight.id);
    };
  
    return (
      <div>
        <h3>{flight.destination}</h3>
        <p>{flight.date}</p>
        <p>{flight.airport}</p>
        <p>{flight.noTotalSeats}</p>
        <button onClick={handleDelete}>Delete</button>
      </div>
    );
  };
  
  export default FlightItem;
  