import {
    Box,
    Button, Divider, FormControl, InputLabel, List, ListItem,
    MenuItem,
    Paper,
    Select,
    TextField,
    Typography
} from "@mui/material"

import {useState} from "react"

export default function CashierClient({items}) {
    const [currentItemIndex, setCurrentItemIndex] = useState("")
    const [currentItem, setCurrentItem] = useState({})
    const [initialDisableBuy, setInitialDisableBuy] = useState(true)
    const [quantity, setQuantity] = useState(0)
    const [showBasket, setShowBasket] = useState(false)
    const [basket, setBasket] = useState([])
    const [basketError, setBasketError] = useState(false)
    const [showCheckout, setShowCheckout] = useState(false)
    const [disableBasketActions, setDisableBasketActions] = useState(false)
    const [quantityError, setQuantityError] = useState(false)
    const [showOrderSuccess, setShowOrderSuccess] = useState(false)
    const [showOrderFailure, setShowOrderFailure] = useState(false)

    const handleClear = () => {
        setInitialDisableBuy(true)
        setCurrentItemIndex("")
        setBasketError(false)
        setInitialDisableBuy(true)
        setQuantity(0)
        setQuantityError(false)
        setShowCheckout(false)
    }

    const handleItemChosen = (event) => {
        setCurrentItemIndex(event.target.value)
        const currentItemIndexSynchronous = event.target.value
        setCurrentItem(items[currentItemIndexSynchronous])
        setInitialDisableBuy(false)
        setQuantityError(false)
    }

    const handleAddToBasket = () => {
        if (Number(quantity) === 0) {
            setBasketError(true)
            return
        }

        setBasket(prevBasket => {
            const existingItemIndex = prevBasket.findIndex(item => item.name === currentItem.name)
            if (existingItemIndex !== -1) {
                const updatedBasket = [...prevBasket]
                if (currentItem.stockLevel < Number(updatedBasket[existingItemIndex].quantity) + Number(quantity)) {
                    setQuantityError(true)
                    return prevBasket
                }
                updatedBasket[existingItemIndex] = {
                    ...updatedBasket[existingItemIndex],
                    quantity: updatedBasket[existingItemIndex].quantity + Number(quantity),
                }
                setBasketError(false)
                setShowBasket(true)
                setInitialDisableBuy(false)
                return updatedBasket
            }

            if (currentItem.stockLevel < quantity) {
                setQuantityError(true)
                return prevBasket
            }

            setBasketError(false)
            setShowBasket(true)
            setInitialDisableBuy(false)
            return [
                ...prevBasket,
                {
                    name: currentItem.name,
                    quantity: Number(quantity),
                    pNum: currentItem.pNum,
                    price: currentItem.price
                },
            ]
        })
    }

    const handleCheckout = () => {
        if (basket.length === 0) {
            return;
        }
        setShowCheckout(true)
        setInitialDisableBuy(true)
        setDisableBasketActions(true)
    }

    const quantityChange = (event) => {
        if (event.target.value < 0 || event.target.value > 99) {
            return
        }
        setQuantity(event.target.value)
    }

    const getBasketPrice = basket.reduce((buffer, item) => {
        return buffer + item.quantity * item.price
    }, 0)

    const handleEmpty = () => {
        setBasket([])
        setShowBasket(false)
        setInitialDisableBuy(true)
        setShowOrderSuccess(false)
        setShowOrderFailure(false)
    }

    const handleDoNotCheckOut = () => {
        setInitialDisableBuy(false)
        setDisableBasketActions(false)
        setShowCheckout(false)
    }

    const postOrderRefreshStates = () => {
        setDisableBasketActions(false)
        setInitialDisableBuy(true)
    }

    const handleFinalizeCheckout = () => {
        fetch("http://localhost:3000/api/customer/buy", {
            method: "POST",
            body: JSON.stringify(basket)
        })
            .then(async (res) => {
                if (res.ok) {
                    setShowOrderSuccess(true)
                    setTimeout(() => {
                        handleClear()
                        handleEmpty()
                        postOrderRefreshStates()
                    }, 1500)
                }
            })
            .catch(async () => {
                setShowOrderFailure(true)
                setTimeout(() => {
                    handleEmpty()
                    handleClear()
                    postOrderRefreshStates()
                }, 1500)
            })
    }

    return (
        <Box sx={{marginTop: 1, padding: 5, minWidth: 595}}>
            <Box sx={{display: "flex", justifyContent: "center", marginBottom: 5}}>
                <Typography variant={"h4"}>Cashier Client</Typography>
            </Box>
            <FormControl fullWidth>
                <Box display={"flex"} justifyContent={"center"} minWidth={500}>
                    <InputLabel id={"stock-name"}>Stock List</InputLabel>
                    <Select disabled={disableBasketActions} sx={{width: 250, marginRight: 5}} value={currentItemIndex} onChange={handleItemChosen} labelId={"stock-name"}>
                        {
                            items.length > 0 && items.map((item, iterator) => (
                                <MenuItem key={item.name} value={iterator}>{item.name}</MenuItem>
                            ))
                        }
                    </Select>
                    <TextField disabled={disableBasketActions} label={"Quantity"} type={"number"} value={quantity} onChange={quantityChange}></TextField>

                </Box>
            </FormControl>
            <Box display={"flex"} justifyContent={"center"} marginBottom={"40px"} marginTop={"40px"}>
                <Button disabled={disableBasketActions} onClick={handleAddToBasket} variant={"contained"} sx={{ marginRight: 1 }}>Add to basket</Button>
                <Button disabled={initialDisableBuy} onClick={handleCheckout} variant={"outlined"} sx={{ marginRight: 1 }}>Checkout</Button>
                <Button disabled={disableBasketActions} onClick={handleEmpty} variant={"text"} sx={{ marginRight: 1 }}>Empty Basket</Button>
                <Button disabled={disableBasketActions} onClick={handleClear} variant={"text"}>Clear</Button>
            </Box>
            {
                showBasket && (
                    <Box>
                        <Paper>
                            <List>
                                {
                                    basket.map((item) => (
                                        <ListItem key={item.name} secondaryAction={
                                                <Typography>
                                                    £{item.quantity * item.price}
                                                </Typography>
                                            }>
                                            {item.name} x{item.quantity}
                                        </ListItem>
                                    ))
                                }
                                <Divider />
                                <ListItem
                                    secondaryAction={
                                        <Typography textAlign={"right"}>
                                            £{
                                                getBasketPrice
                                            }
                                        </Typography>
                                    }
                                >
                                    Total
                                </ListItem>
                            </List>
                        </Paper>
                    </Box>
                )
            }
            {
                basketError && (
                    <Typography variant={"subtitle1"} color={"error"} textAlign={"center"}>Select an item and quantity to add to the basket.</Typography>
                )
            }
            {
                showCheckout && (
                    <>
                        <Box marginTop={5}>
                            <Typography variant={"subtitle1"} textAlign={"center"}>Are you ready to check-out?</Typography>
                        </Box>
                        <Box display={"flex"} justifyContent={"center"} marginTop={2} marginBottom={2}>
                            <Button onClick={handleFinalizeCheckout} variant={"contained"} sx={{ marginRight: 1 }}>Yes</Button>
                            <Button onClick={handleDoNotCheckOut} variant={"outlined"}>No</Button>
                        </Box>
                    </>
                )
            }
            {
                quantityError && (
                    <Typography variant={"subtitle1"} color={"error"} textAlign={"center"}>Not enough items in stock.</Typography>
                )
            }
            {
                showOrderSuccess && (
                    <Typography variant={"subtitle1"} color={"success"} textAlign={"center"}>Order successful!</Typography>
                )
            }
            {
                showOrderFailure && (
                    <Typography variant={"subtitle1"} color={"error"} textAlign={"center"}>Order failed. Is the database up?</Typography>
                )
            }

        </Box>
    )
}