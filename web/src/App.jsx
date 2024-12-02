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

    useEffect(() => {
        fetch("http://localhost:3000/api/stock")
            .then((res) => res.json())
            .then((res) => {
                setItems(res)
            })
            .catch(() => {
                setFetchError(true)
            })
    }, [refetchStocklist, activeTab])

    /**
     * Reset active tab
     */
    useEffect(() => {
        if (window.localStorage["tab"] !== undefined) {
            tabChange(null, window.localStorage["tab"])
        }
    }, []);

    const tabChange = (_event, newTabIndex) => {
        setActiveTab(Number(newTabIndex))
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
                <TabPanel items={items} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={0} type={tabEnum.BACKDOOR}/>
                <TabPanel items={items} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={1} type={tabEnum.CUSTOMER}/>
                <TabPanel items={items} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={2} type={tabEnum.CASHIER}/>
                <TabPanel items={items} setRefetchStocklist={setRefetchStocklist} setFetchError={setFetchError} value={activeTab} index={3} type={tabEnum.PACKING}/>
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