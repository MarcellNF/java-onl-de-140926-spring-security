import './App.css'
import {useEffect, useState} from "react";
import axios from "axios";
import type {AppUser} from "./appuser.ts";
import {Routes, Route} from "react-router";
import ProtectedRoutes from "./ProtectedRoutes.tsx";
import Home from "./Home.tsx";

function App() {
    const [user, setUser] = useState<AppUser | null | undefined>(undefined);

    useEffect(() => {
        axios.get("api/auth/me")
            .then(res => setUser(res.data))
            .catch(() => setUser(null));
    }, []);

    return (
        <Routes>
            <Route path="/" element={<Home user={user} />}/>
            <Route element={<ProtectedRoutes user={user}/>}>
                <Route path="/secured"
                       element="Du bist auf einer gesicherten Seite, die du nur erreichst, wenn du eingeloggt bist!"/>
            </Route>

            <Route element={<ProtectedRoutes user={user} role="ADMIN"/>}>
                <Route path="/secured-admin"
                       element="Du bist auf einer gesicherten Seite, die du nur erreichst, wenn du eingeloggt und ADMIN bist!"/>
            </Route>
        </Routes>
    )
}

export default App
