import AbstractVista from "./AbstractVista";
import TablaUser from "./tables/TablaUser";

const UserVista = AbstractVista(TablaUser, "USERS", null, "usuarios");

export default UserVista;
