import { useEffect, useState } from 'react';
import { Route, Routes, Link, Navigate, useNavigate } from 'react-router-dom';

import Home from './components/Home';
import Stuff from './components/Stuff';
import About from './components/About';
import Contact from './components/Contact';

function App() {
  const [user, setUser] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetch('/api/user')
      .then(response => {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 404) {
          return null;
        }
        return Promise.reject(`Unexpected status code: ${response.status}`);
      })
      .then(data => {
        if (data) {
          setUser({
            appUserId: data.appUserId,
            role: data.role,
            name: data.name,
            email: data.email,
            gitHubUsername: data.gitHubUsername
          });
        } else {
          navigate('/unknownuser');
        }
      });
  }, []);

  const logout = () => {
    fetch('/api/user/logout', { method: 'DELETE' })
      .then(response => {
        if (response.status === 204) {
          setUser(null);
        } else {
          console.log(`Unexpected status code: ${response.status}`);
        }
      });
  };

  return (
    <>
      <h1>OIDC Demo</h1>
      {user ? (
        <div>
          <p>Logged in as: {user.name} ({user.email}, {user.gitHubUsername})</p>
          <p><button onClick={logout}>Logout</button></p>
        </div>
      ) : (
        <div>
          <div>
            With GitHub: <a href="/oauth2/authorization/github">click here</a>
          </div>
          <div>
            With Google: <a href="/oauth2/authorization/google">click here</a>
          </div>
        </div>
      )}
      <nav>
        <ul>
          <li><Link to="/">Home</Link></li>
          <li><Link to="/stuff">Stuff</Link></li>
          <li><Link to="/about">About</Link></li>
          <li><Link to="/contact">Contact</Link></li>
        </ul>
      </nav>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/stuff" element={user ? <Stuff user={user} /> : <Navigate to="/unknownuser" />} />
        <Route path="/about" element={<About />} />
        <Route path="/contact" element={<Contact />} />
        <Route path="/unknownuser" element={<h2>Unknown User</h2>} />
      </Routes>
    </>
  );
}

export default App;
