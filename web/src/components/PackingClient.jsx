import {
    Box,
    Button, Collapse,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import {useEffect, useState} from "react";

export default function PackingClient() {
    const [packingItems, setPackingItems] = useState([])
    const [refetchPacking, setRefetchPacking] = useState(false)
    const [whichBasket, setWhichBasket] = useState(-1)
    const [expandBasket, setExpandBasket] = useState(false)
    const [firstBasketOpen, setFirstBasketOpen] = useState(false)

    useEffect(() => {
        fetch("http://localhost:3000/api/staff/pack")
            .then(res => res.json())
            .then((res) => {
                const merged = mergeOrders(res)
                console.log(merged)
                setPackingItems(merged)
            })
    }, [refetchPacking]);

    const jsonHasChild = (parent, child) => {
        for (const innerChild of parent) {
            if (innerChild.UUID === child.UUID) {
                return true
            }
        }
        return false
    }

    const jsonChildAtWhatIndex = (parent, child) => {
        for (let i = 0; i < parent.length; i++) {
            if (parent[i].UUID === child.UUID) return i
        }
        return null
    }

    const productNumIndex = (parent, pNum) => {
        for (let i = 0; i < parent.length; i++) {
            if (parent.items[i].pNum === pNum) return i
        }
        return null
    }

    const mergeOrders = (parent) => {
        let comparisonArray = []
        for (const child of parent) {
            if (jsonHasChild(comparisonArray, child)) {
                const index = jsonChildAtWhatIndex(comparisonArray, child)
                const pNumIndex = productNumIndex(comparisonArray[index], child.pNum)
                if (pNumIndex) {
                    comparisonArray[index].items[pNumIndex].quantity += child.quantity
                } else {
                    comparisonArray[index].items.push({
                        pNum: child.pNum, quantity: child.quantity
                    })
                }
            } else {
                comparisonArray.push({
                    UUID: child.UUID,
                    date: child.date,
                    items: [{
                        pNum: child.pNum, quantity: child.quantity
                    }]
                })
            }
        }
        return comparisonArray
    }

    const handleExpandBasket = (event) => {
        const newWhichBasket = Number(event.target.getAttribute("data-index"))
        if (whichBasket === newWhichBasket) {
            setExpandBasket(false)
            setWhichBasket(-1)
            return
        }
        setFirstBasketOpen(true)
        setWhichBasket(newWhichBasket)
        setExpandBasket(true)
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
                                    <>
                                        <TableRow key={i}>
                                            <TableCell>
                                                <Button onClick={handleExpandBasket} data-index={i}>{item.UUID}</Button>

                                            </TableCell>
                                            <TableCell>
                                                <Typography variant={"subtitle2"}>{item.date}</Typography>
                                            </TableCell>

                                        </TableRow>
                                        <TableRow sx={{ display: (firstBasketOpen || expandBasket && whichBasket === i) ? "":"none" }}>
                                            <TableCell>
                                                <Collapse in={expandBasket && whichBasket === i}>
                                                    <p>test</p>
                                                </Collapse>
                                            </TableCell>
                                            <TableCell></TableCell>
                                        </TableRow>
                                    </>

                                ))
                            }
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </>
    )
}