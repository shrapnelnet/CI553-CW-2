import {useEffect, useState} from "react"
import {
    Box,
    Button,
    FormControl,
    InputLabel,
    MenuItem, Paper,
    Select, Table, TableBody, TableCell,
    TableContainer, TableHead, TableRow,
    TextField,
    Typography
} from "@mui/material"

export default function BackdoorClient() {
    const [items, setItems] = useState([])
    const [currentItemIndex, setCurrentItemIndex] = useState(-1)
    const [buttonsDisabled, setButtonsDisabled] = useState(true)
    const [queryAll, setQueryAll] = useState(false)
    const [currentItem, setCurrentItem] = useState({})
    const [tableDisplay, setTableDisplay] = useState(false)
    const [fetchError, setFetchError] = useState(false)
    const [quantity, setQuantity] = useState(0)
    const [refetchStocklist, setRefetchStocklist] = useState(false)
    const [quantityZeroError, setQuantityZeroError] = useState(false)
    const [showTotal, setShowTotal] = useState(false)
    const [orderTotal, setOrderTotal] = useState(0)
    const [showOrderSuccess, setShowOrderSuccess] = useState(false)
    const [shouldDisableOrderButtons, setShouldDisableOrderButtons] = useState(false)

    // state asynchronous shit can suck my penis !!!!
    const stockChosen = (event) => {
        setButtonsDisabled(false)
        setCurrentItemIndex(event.target.value)
        const currentItemIndexSynchronous = event.target.value
        setCurrentItem(items[currentItemIndexSynchronous])
    }

    useEffect(() => {
        fetch("http://localhost:3000/api/stock")
            .then((res) => res.json())
            .then((res) => {
                setItems(res)
            })
            .catch(() => {
                setFetchError(true)
            })
    }, [refetchStocklist])

    const queryItem = () => {
        setQueryAll(false)
        setTableDisplay(true)
        console.log(currentItemIndex)
    }

    const queryAllItems = () => {
        setQueryAll(true)
        setTableDisplay(true)
    }

    const clear = () => {
        setTableDisplay(false)
        setButtonsDisabled(true)
        setQuantity(0)
        setQuantityZeroError(false)
        setFetchError(false)
        setShowTotal(false)
        setShowOrderSuccess(false)
        setCurrentItemIndex(-1)
    }

    const order = () => {
        setTableDisplay(false)
        setShouldDisableOrderButtons(false)
        if (quantity === 0) {
            setQuantityZeroError(true)
            return
        }
        const total = currentItem.price * quantity
        setTableDisplay(false)
        setOrderTotal(total)
        setShowTotal(true)
    }

    const finalizeOrder = () => {
        setShouldDisableOrderButtons(true)
        fetch("http://localhost:3000/api/buy", {
            method: "POST",
            body: JSON.stringify({
                quantity,
                pNum: currentItem.pNum
            })
        })
            .then((res) => {
                if (res.ok) {
                    setRefetchStocklist(refetchStocklist => !refetchStocklist)
                    setShowOrderSuccess(true)
                    return
                }
                setFetchError(true)
            })
    }

    const quantityChange = (event) => {
        if (event.target.value < 0 || event.target.value > 99) {
            return
        }
        setQuantity(event.target.value)
    }

    return (
        <Box sx={{marginTop: 1, padding: 5}}>
            <Box sx={{display: "flex", justifyContent: "center", marginBottom: 5}}>
                <Typography variant={"h4"}>Backdoor Client Model</Typography>
            </Box>
            <FormControl fullWidth>
                <Box display={"flex"} justifyContent={"center"}>
                    <InputLabel id={"stock-name"}>Stock List</InputLabel>
                    <Select sx={{ width: 250, marginRight: 5 }} value={currentItemIndex} onChange={stockChosen} labelId={"stock-name"}>
                        {
                            items.length > 0 && items.map((item, iterator) => (
                                <MenuItem key={iterator} value={iterator}>{item.name}</MenuItem>
                            ))
                        }
                    </Select>
                    <TextField label={"Quantity"} type={"number"} value={quantity} onChange={quantityChange}></TextField>
                </Box>
                <Box sx={{ display: "flex", justifyContent: "center", marginTop: 5, marginBottom: 5 }}>
                    <Button onClick={queryItem} disabled={buttonsDisabled} sx={{ marginRight: 1 }} variant={"contained"}>Query</Button>
                    <Button onClick={order} disabled={buttonsDisabled} variant={"contained"} sx={{ marginRight: 1 }}>Order</Button>
                    <Button onClick={queryAllItems} variant={"outlined"} sx={{ marginRight: 1 }}>Query All</Button>
                    <Button onClick={clear} variant={"outlined"}>Clear</Button>
                </Box>
                {
                    fetchError && (
                        <Box>
                            <Typography color={"error"} textAlign={"center"} variant={"subtitle1"}>An error occurred fetching the stock-list. Is the database up?</Typography>
                        </Box>
                    )
                }
                {
                    quantityZeroError && (
                        <Box>
                            <Typography color={"error"} textAlign={"center"} variant={"subtitle1"}>Select a quantity and item to order more stock</Typography>
                        </Box>
                    )
                }
                {
                    showTotal && (
                        <Box>
                            <Typography textAlign={"center"} variant={"subtitle1"}>Order total: £{orderTotal}. Place this order?</Typography>
                            <Box justifyContent={"center"} display={"flex"} marginTop={2}>
                                <Button disabled={shouldDisableOrderButtons} onClick={finalizeOrder} variant={"contained"} sx={{ marginRight: 1 }}>Yes</Button>
                                <Button disabled={shouldDisableOrderButtons} onClick={clear} variant={"outlined"}>No</Button>
                            </Box>
                        </Box>
                    )
                }
                {
                    showOrderSuccess && (
                        <Box>
                            <Typography color={"success"} textAlign={"center"} variant={"subtitle1"}>Order placed!</Typography>
                        </Box>
                    )
                }
                {
                    tableDisplay ?
                        (
                            <TableContainer component={Paper}>
                                <Table sx={{ minWidth: 500 }}>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Product name</TableCell>
                                            <TableCell align={"right"}>Amount in stock</TableCell>
                                            <TableCell align={"right"}>Price (£)</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {
                                            queryAll ?
                                                (
                                                    items.map((currentQueryAllItem, iterator) => (
                                                        <TableRow key={iterator}>
                                                            <TableCell>{currentQueryAllItem.name}</TableCell>
                                                            <TableCell align={"right"}>{currentQueryAllItem.stockLevel}</TableCell>
                                                            <TableCell align={"right"}>{currentQueryAllItem.price}</TableCell>
                                                        </TableRow>
                                                    ))
                                                )
                                                :
                                                (
                                                    <TableRow>
                                                        <TableCell>{currentItem.name}</TableCell>
                                                        <TableCell align={"right"}>{currentItem.stockLevel}</TableCell>
                                                        <TableCell align={"right"}>{currentItem.price}</TableCell>
                                                    </TableRow>
                                                )
                                        }
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        )
                        :
                        (
                            <></>
                        )
                }
            </FormControl>
        </Box>
    )
}