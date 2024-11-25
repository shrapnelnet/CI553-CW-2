import {tabEnum} from "../App.jsx";
import BackdoorClient from "./BackdoorClient.jsx";

export default function TabPanel({index, value, type}) {
    return (
        <section hidden={index !== value}>
            {
                type === tabEnum.BACKDOOR && <BackdoorClient/>
            }
        </section>
    )
}

