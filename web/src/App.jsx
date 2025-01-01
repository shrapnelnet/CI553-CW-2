import "./index.css"
import {Box, Tab, Tabs, Typography,} from "@mui/material";
import {useEffect, useState} from "react";
import TabPanel from "./components/TabPanel.jsx";
import {tabEnum} from "./components/tabenum.js";


export default function App() {
    const [activeTab, setActiveTab] = useState(0)
    const [items, setItems] = useState([])
    const [fetchError, setFetchError] = useState(false)
    const [refetchStocklist, setRefetchStocklist] = useState(false)
    const [refetchPacking, setRefetchPacking] = useState(false)
    const [packingItems, setPackingItems] = useState([])


    useEffect(() => {
        fetch("http://localhost:3000/api/stock")
            .then((res) => res.json())
            .then((res) => {
                setItems(res)
            })
            .catch(() => {
                setFetchError(true)
            })
    }, [refetchStocklist, activeTab, refetchPacking])

    /**
     * Reset active tab
     */
    useEffect(() => {
        if (window.localStorage["tab"] !== undefined) {
            tabChange(null, window.localStorage["tab"])
        }
    }, []);

    useEffect(() => {
        fetch("http://localhost:3000/api/staff/pack")
            .then(res => res.json())
            .then((res) => {
                const merged = mergeOrders(res);
                setPackingItems(merged);
                console.log(merged)
            });
    }, [refetchPacking]);

    const jsonHasChild = (parent, child) => {
        for (const innerChild of parent) {
            if (innerChild.UUID === child.UUID) {
                return true;
            }
        }
        return false;
    };

    const jsonChildAtWhatIndex = (parent, child) => {
        for (let i = 0; i < parent.length; i++) {
            if (parent[i].UUID === child.UUID) return i;
        }
        return null;
    };

    const productNumIndex = (parent, pNum) => {
        for (let i = 0; i < parent.length; i++) {
            if (parent.items[i].pNum === pNum) return i;
        }
        return null;
    };

    const mergeOrders = (parent) => {
        let comparisonArray = [];
        for (const child of parent) {
            if (jsonHasChild(comparisonArray, child)) {
                const index = jsonChildAtWhatIndex(comparisonArray, child);
                const pNumIndex = productNumIndex(comparisonArray[index], child.pNum);
                if (pNumIndex !== null) {
                    comparisonArray[index].items[pNumIndex].quantity += child.quantity;
                } else {
                    comparisonArray[index].items.push({
                        pNum: child.pNum, quantity: child.quantity
                    });
                }
            } else {
                comparisonArray.push({
                    UUID: child.UUID,
                    date: child.date,
                    items: [{
                        pNum: child.pNum, quantity: child.quantity
                    }]
                });
            }
        }
        return comparisonArray;
    };

    const tabChange = (_event, newTabIndex) => {
        let tabIndex = Number(newTabIndex)
        setActiveTab(tabIndex)
        if (tabIndex === tabEnum.PACKING) {
            setRefetchPacking(value => !value)
        }
        window.localStorage["tab"] = newTabIndex
    }

    return (
        <>
            <Box sx={{display: "flex", justifyContent: "center"}}>
                <Box sx={{borderBottom: 1, borderColor: "divider"}}>
                    <Tabs value={activeTab} onChange={tabChange}>
                        <Tab label={"Backdoor Client"}/>
                        <Tab label={"Customer Client"}/>
                        <Tab label={"Cashier Client"}/>
                        <Tab label={"Packing Client"}/>
                    </Tabs>
                </Box>
            </Box>
            <Box sx={{display: "flex", justifyContent: "center"}}>
                <TabPanel items={items} packingItems={packingItems} refetchPacking={refetchPacking} setRefetchPacking={setRefetchPacking} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={0} type={tabEnum.BACKDOOR}/>
                <TabPanel items={items} packingItems={packingItems} refetchPacking={refetchPacking} setRefetchPacking={setRefetchPacking} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={1} type={tabEnum.CUSTOMER}/>
                <TabPanel items={items} packingItems={packingItems} refetchPacking={refetchPacking} setRefetchPacking={setRefetchPacking} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={2} type={tabEnum.CASHIER}/>
                <TabPanel items={items} packingItems={packingItems} refetchPacking={refetchPacking} setRefetchPacking={setRefetchPacking} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={3} type={tabEnum.PACKING}/>
                {
                    fetchError && (
                        <Box>
                            <Typography color={"error"} textAlign={"center"} variant={"subtitle1"}>An error occurred fetching the stock-list. Is the database up?</Typography>
                        </Box>
                    )
                }
            </Box>
        </>
    )
}