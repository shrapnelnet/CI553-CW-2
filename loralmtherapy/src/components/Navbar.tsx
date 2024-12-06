import {Link} from "react-router";

export default function Navbar() {
    return (
        <header>
            <nav className="navbar">
                <ul>
                    <li>
                        <Link to={"/"}>Home</Link>
                    </li>
                    <li>
                        <Link to={"/about"}>About</Link>
                    </li>
                    <li>
                        <Link to={"/contact"}>Contact</Link>
                    </li>
                </ul>
            </nav>
        </header>

    )
}