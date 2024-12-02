import {tabEnum} from "./tabenum.js";
import BackdoorClient from "./BackdoorClient.jsx";
import CustomerClient from "./CustomerClient.jsx";
import CashierClient from "./CashierClient.jsx";

export default function TabPanel({index, value, type, items, setRefetchStocklist, setFetchError}) {
    return (
        <section hidden={index !== value}>
            {
                type === tabEnum.BACKDOOR && <BackdoorClient items={items} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} />
            }
            {
                type === tabEnum.CUSTOMER && <CustomerClient items={items} />
            }
            {
                type === tabEnum.CASHIER && <CashierClient items={items} />
            }
        </section>
    )
}

