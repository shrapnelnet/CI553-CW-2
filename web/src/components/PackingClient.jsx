import {
    Box,
    Button,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material"
import {useState} from "react"

export default function PackingClient({packingItems, setRefetchpacking}) {
    const [whichBasket, setWhichBasket] = useState(-1)
    const [expandBasket, setExpandBasket] = useState(false)
    const [currentBasket, setCurrentBasket] = useState({})
    const [hasClosedAtLeastOneBasket, setHasClosedAtLeastOneBasket] = useState(false)
    const [finalizePacking, setFinalizePacking] = useState(false)
    const [showSuccess, setShowSuccess] = useState(false)
    const [showError, setShowError] = useState(false)


    const handleExpandBasket = (event) => {
        const currentBasketIndex = Number(event.target.getAttribute("data-index"));
        if (currentBasketIndex === whichBasket && !hasClosedAtLeastOneBasket) {
            setExpandBasket(false)
            setHasClosedAtLeastOneBasket(true)
            return
        }
        setHasClosedAtLeastOneBasket(false)
        setExpandBasket(true)
        setWhichBasket(currentBasketIndex)
        const currentItem = packingItems[currentBasketIndex]
        setCurrentBasket(currentItem)
    };

    const handlePack = () => {
        setFinalizePacking(true)
    };

    const cancelPacking = () => {
        setFinalizePacking(false)
    }

    const doFinalizePacking = () => {
        const { UUID } = currentBasket
        if (UUID === undefined) {
            return
        }
        fetch(`http://localhost:3000/api/staff/finalizePack?orderid=${UUID}`, {method: "DELETE"})
            .then(() => {
                setShowSuccess(true)
            })
            .catch(() => {
                setShowError(true)
            })
            .finally(() => {
                setTimeout(() => {
                    setShowSuccess(false)
                    setShowError(false)
                    setExpandBasket(false)
                    setFinalizePacking(false)
                    setRefetchpacking(refetchPacking => !refetchPacking)
                }, 2000)
            })
    }

    return (
        <>
            <Box sx={{marginTop: 1, padding: 5}}>
                <Box sx={{display: "flex", justifyContent: "center", marginBottom: 5}}>
                    <Typography variant={"h4"}>Packing Client</Typography>
                </Box>
            </Box>
            <Box>
                <TableContainer component={Paper} sx={{marginBottom: "40px"}}>
                    <Table sx={{minWidth: 500}}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Order ID</TableCell>
                                <TableCell align={"right"}>Date</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                packingItems.map((item, i) => (
                                    <TableRow key={i}>
                                        <TableCell>
                                            <Button disabled={finalizePacking} onClick={handleExpandBasket} data-index={i}>{item.UUID}</Button>
                                        </TableCell>
                                        <TableCell>
                                            <Typography align={"right"} variant={"subtitle2"}>{item.date}</Typography>
                                        </TableCell>
                                    </TableRow>
                                ))
                            }
                            {
                                packingItems.length === 0 && (
                                    <TableRow>
                                        <TableCell>
                                            <Typography>No items waiting to pack.</Typography>
                                        </TableCell>
                                        <TableCell></TableCell>
                                    </TableRow>
                                )
                            }
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
            <Box>
                {
                    expandBasket && whichBasket !== -1 && (
                        <Typography sx={{ marginBottom: "10px" }}>Basket: {packingItems[whichBasket].UUID}</Typography>
                    )
                }
                <TableContainer component={Paper} sx={{marginBottom: "40px"}}>
                    <Table sx={{minWidth: 500}}>
                        <TableHead>
                            <TableRow>
                                <TableCell>Product Number</TableCell>
                                <TableCell align={"right"}>Quantity</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {
                                expandBasket && whichBasket !== -1 && currentBasket.items.map((item, i) => (
                                    <TableRow key={`${item}-${i}`}>
                                        <TableCell>
                                            <Typography>{item.pNum}</Typography>
                                        </TableCell>
                                        <TableCell>
                                            <Typography align={"right"}
                                                        variant={"subtitle2"}>{item.quantity}</Typography>
                                        </TableCell>
                                    </TableRow>
                                ))
                            }
                            {
                                expandBasket && whichBasket !== -1 && (
                                    <TableRow>
                                        <TableCell>
                                            <Button onClick={handlePack}>Pack</Button>
                                        </TableCell>
                                        <TableCell>
                                        </TableCell>
                                    </TableRow>
                                )
                            }
                        </TableBody>
                    </Table>
                </TableContainer>
                {
                    finalizePacking && (
                        <Box display={"flex"} justifyContent={"center"} flexWrap={"wrap"}>
                            <Typography>Pack this order?</Typography>
                            <Box width={"100%"}></Box>
                            <Box display={"flex"} justifyContent={"center"} marginTop={"10px"}>
                                <Button variant={"contained"} sx={{ marginRight: "10px" }} onClick={doFinalizePacking}>Yes</Button>
                                <Button variant={"outlined"} onClick={cancelPacking}>No</Button>
                            </Box>
                        </Box>
                    )
                }
                {
                    showSuccess && (
                        <Typography color={"success"}>Order packed!</Typography>
                    )
                }
                {
                    showError && (
                        <Typography color={"error"}>Order failed to pack. Is the database up?</Typography>
                    )
                }
            </Box>
        </>
    );
}
