import {FaInstagram, FaFacebook, FaXTwitter} from "react-icons/fa6";

export default function Footer() {
    const size = 40;

    return (
        <footer>
            <div className={"footer-socials"}>
                <ul>
                    <li>
                        <a href="https://facebook.com">
                            <FaFacebook size={size} title={"Facebook"} />
                        </a>
                    </li>
                    <li>
                        <a href="https://x.com">
                            <FaXTwitter size={size} title={"Twitter/X"} />
                        </a>
                    </li>
                    <li>
                        <a href="https://instagram.com">
                            <FaInstagram size={size} title={"Instagram"} />
                        </a>
                    </li>
                </ul>
            </div>
        </footer>
    )
}