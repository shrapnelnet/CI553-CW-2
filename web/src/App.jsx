import "./index.css"
import {Box, Tab, Tabs,} from "@mui/material";
import {useState} from "react";
import TabPanel from "./components/TabPanel.jsx";

export const tabEnum = {
    BACKDOOR: 0,
    CUSTOMER: 1,
    CASHIER: 2,
    PACKING: 3
}


export default function App() {
    const [activeTab, setActiveTab] = useState(0)

    const tabChange = (event, newTabIndex) => {
        setActiveTab(newTabIndex)
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
                <TabPanel value={activeTab} index={0} type={tabEnum.BACKDOOR}/>
                <TabPanel value={activeTab} index={1} type={tabEnum.CUSTOMER}/>
                <TabPanel value={activeTab} index={2} type={tabEnum.CASHIER}/>
                <TabPanel value={activeTab} index={3} type={tabEnum.PACKING}/>
            </Box>
        </>
    )
}