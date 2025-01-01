import {tabEnum} from "./tabenum.js";
import BackdoorClient from "./BackdoorClient.jsx";
import CustomerClient from "./CustomerClient.jsx";
import CashierClient from "./CashierClient.jsx";
import PackingClient from "./PackingClient.jsx";

export default function TabPanel({index, value, type, items, setRefetchStocklist, setFetchError, packingItems, setRefetchPacking, refetchPacking}) {
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
            {
                type === tabEnum.PACKING && <PackingClient items={items} packingItems={packingItems} refetchPacking={refetchPacking} setRefetchpacking={setRefetchPacking} />
            }
        </section>
    )
}

