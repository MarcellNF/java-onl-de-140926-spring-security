import type {AppUser} from "./appuser.ts";

type HomeProps = {
    user?: AppUser | null | undefined
}
export default function Home({user}: Readonly<HomeProps>) {

    function login() {
        const host = window.location.hostname === 'localhost' ? 'http://localhost:8080' : window.location.origin;
        window.open(host + '/oauth2/authorization/github', '_self');
    }

    function logout() {
        const host = window.location.hostname === 'localhost' ? 'http://localhost:8080' : window.location.origin;
        window.open(host + '/logout', '_self');
    }

    return (
        <>
            {user ?
                <button onClick={logout}>Logout</button> :
                <button onClick={login}>Login with GitHub</button>}
        </>
    );

}