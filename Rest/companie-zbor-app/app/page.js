import Link from 'next/link';

const Home = () => {
  return (
    <div>
      <h1>Welcome to the Flight Management System</h1>
      <Link href="/flights">
        <p>View Flights</p>
      </Link>
      <br />
      <Link href="/flights/add">
        <p>Add a New Flight</p>
      </Link>
    </div>
  );
};

export default Home;
