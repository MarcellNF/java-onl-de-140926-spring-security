import type {AppUser} from "./appuser.ts";
import {Navigate, Outlet} from "react-router";

type ProtectedRoutesProps = {
    user: AppUser | null | undefined;
    role?: AppUser["role"];
}

export default function ProtectedRoutes({user, role}: Readonly<ProtectedRoutesProps>) {

    if(user === undefined) {
        return <div>Loading...</div>
    }

    if (user === null) {
        return <Navigate to="/" replace />
    }

    if (role && user.role !== role) {
        return <Navigate to="/" replace />
    }

    return <Outlet />

}