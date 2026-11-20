import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";

function App() {
  const [username, setUsername] = useState<string>('');
  const [avatar, setAvatar] = useState<string>('');

  function login() {
    const host = window.location.hostname === 'localhost' ? 'http://localhost:8080' : window.location.origin;
    window.open(host + '/oauth2/authorization/github', '_self');
  }

  useEffect(() => {
    axios.get("api/auth/me")
        .then(res => {
          setUsername(res.data.username)
          setAvatar(res.data.avatar_url)
        })
  });

  return (
      <>
        <button onClick={login}>Login with GitHub</button>
        {username ? <p>{username}</p> : <p>Nicht eingeloggt</p>}
        <img src={avatar} />
      </>
  )
}

export default App
