import {
    Box, Button, Collapse,
    FormControl, InputLabel, MenuItem,
    Paper, Select,
    Table, TableBody, TableCell, TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import {useState} from "react";

export default function CustomerClient({items}) {
    const [expandImage, setExpandImage] = useState(false)
    const [whichImage, setWhichImage] = useState(-1)
    const [showCatalogue, setShowCatalogue] = useState(false)
    const [showSpecific, setShowSpecific] = useState(false)
    const [currentItemIndex, setCurrentItemIndex] = useState("")
    const [currentItem, setCurrentItem] = useState({})
    const [showTable, setShowTable] = useState(false)
    const [expandSearchImage, setExpandSearchImage] = useState(false)

    const handleItemChosen = (event) => {
        setCurrentItemIndex(event.target.value)
        const currentItemIndexSynchronous = event.target.value
        setCurrentItem(items[currentItemIndexSynchronous])
        setShowTable(true)
    }

    const handleExpandImage = (event) => {
        const newWhichImage = Number(event.target.getAttribute("data-index"))
        if (whichImage === newWhichImage) {
            setExpandImage(false)
            setWhichImage(-1)
            return
        }
        setWhichImage(newWhichImage)
        setExpandImage(true)
    }

    const handleShowCatalogue = () => {
        setShowSpecific(false)
        setShowCatalogue(true)
    }

    const handleClear = () => {
        setShowSpecific(false)
        setShowCatalogue(false)
        setCurrentItemIndex("")
        setShowTable(false)
        setWhichImage(-1)
        setExpandImage(false)
    }

    const handleShowSearch = () => {
        setShowCatalogue(false)
        setShowSpecific(true)
    }

    const handleExpandSearchImage = () => {
        setExpandSearchImage(expandSearchImage => !expandSearchImage)
    }

    return (
        <Box sx={{marginTop: 1, padding: 5}}>
            <Box sx={{display: "flex", justifyContent: "center", marginBottom: 5}}>
                <Typography variant={"h4"}>Customer Client</Typography>
            </Box>
            <Box display={"flex"} justifyContent={"center"} marginBottom={"40px"}>
                <Button onClick={handleShowCatalogue} variant={"contained"} sx={{marginRight: 1}}>Show
                    Catalogue</Button>
                <Button onClick={handleShowSearch} variant={"outlined"} sx={{marginRight: 1}}>Search for an Item</Button>
                <Button onClick={handleClear} variant={"text"}>Clear</Button>
            </Box>
            <Box>
                {
                    showCatalogue && (
                        <FormControl fullWidth>
                            <Box display={"flex"} justifyContent={"center"}>
                                <TableContainer component={Paper}>
                                    <Table sx={{minWidth: 500}}>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Product name</TableCell>
                                                <TableCell align={"right"}>Amount in stock</TableCell>
                                                <TableCell align={"right"}>Price (£)</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {
                                                items.map((currentQueryAllItem, iterator) => (
                                                    <TableRow key={currentQueryAllItem.name}>
                                                        <TableCell>
                                                            <Button data-index={iterator} onClick={(event) => {
                                                                handleExpandImage(event)
                                                            }}>{currentQueryAllItem.name}</Button>
                                                            <Collapse in={expandImage && whichImage === iterator}>
                                                                <img
                                                                    src={`/pic${currentQueryAllItem.pNum}.jpg`}
                                                                    alt={`${currentQueryAllItem.name}`}/>
                                                            </Collapse>
                                                        </TableCell>
                                                        <TableCell
                                                            align={"right"}>{currentQueryAllItem.stockLevel}</TableCell>
                                                        <TableCell align={"right"}>{currentQueryAllItem.price}</TableCell>
                                                    </TableRow>
                                                ))
                                            }
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Box>
                        </FormControl>
                    )
                }
                {
                    showSpecific && (
                        <>
                            <FormControl fullWidth>
                                <Box display={"flex"} justifyContent={"center"} minWidth={500}>
                                    <InputLabel id={"stock-name"} sx={{ left: 105 }}>Stock List</InputLabel>
                                    <Select sx={{width: 250, marginRight: 5}} value={currentItemIndex} onChange={handleItemChosen} labelId={"stock-name"}>
                                        {
                                            items.length > 0 && items.map((item, iterator) => (
                                                <MenuItem key={item.name} value={iterator}>{item.name}</MenuItem>
                                            ))
                                        }
                                    </Select>
                                </Box>
                            </FormControl>
                            {
                                showTable && (
                                    <Box marginTop={"40px"}>
                                        <TableContainer component={Paper} sx={{ marginBottom: "40px"}}>
                                            <Table sx={{ minWidth: 500 }}>
                                                <TableHead>
                                                    <TableRow>
                                                        <TableCell>Product name</TableCell>
                                                        <TableCell align={"right"}>Amount in stock</TableCell>
                                                        <TableCell align={"right"}>Price (£)</TableCell>
                                                    </TableRow>
                                                </TableHead>
                                                <TableBody>
                                                    <TableRow>
                                                        <TableCell>
                                                            <Button onClick={handleExpandSearchImage}>{currentItem.name}</Button>

                                                            <Collapse in={expandSearchImage}>
                                                                <img
                                                                    src={`/pic${currentItem.pNum}.jpg`}
                                                                    alt={`${currentItem.name}`}/>
                                                            </Collapse>
                                                        </TableCell>
                                                        <TableCell align={"right"}>{currentItem.stockLevel}</TableCell>
                                                        <TableCell align={"right"}>{currentItem.price}</TableCell>

                                                    </TableRow>
                                                </TableBody>
                                            </Table>
                                        </TableContainer>
                                    </Box>
                                )
                            }
                        </>
                    )
                }
            </Box>
        </Box>
    )
}