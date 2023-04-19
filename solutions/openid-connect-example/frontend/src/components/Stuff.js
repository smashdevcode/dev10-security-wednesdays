import { useEffect, useState } from 'react';

function Stuff({user}) {
  const [stuff, setStuff] = useState([]);
  const [stuffText, setStuffText] = useState('');

  useEffect(() => {
    fetch('/api/stuff')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        return [];
      })
      .then(data => setStuff(data));
  }, []);

  const handleSubmit = (event) => {
    event.preventDefault();

    if (stuffText) {
      const init = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify([stuffText])
      };

      fetch('/api/stuff', init)
        .then(response => {
          if (response.status === 201) {
            return response.json();
          }
          return Promise.reject(`Unexpected status code: ${response.status}`);
        })
        .then(data => {
          setStuffText('');
          setStuff(data);
        });
    }
  };

  return (
    <>
      <h2>Stuff</h2>
      {user ? (
        <>
          <form onSubmit={handleSubmit}>
            <input type="text" onChange={(event) => setStuffText(event.target.value)} value={stuffText} />
            <button>Add Stuff</button>
          </form>
          {stuff.length > 0 && (
            <ul>
              {stuff.map(s => (
                <li key={s.description}>{s.description}</li>
              ))}
            </ul>
          )}
        </>
      ) : (
        <p><em>Please login to view stuff!</em></p>
      )}
    </>
  );
}

export default Stuff;