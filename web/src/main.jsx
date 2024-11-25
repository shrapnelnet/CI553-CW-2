import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { QueryClientProvider, QueryClient } from "@tanstack/react-query"
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';


const router = createBrowserRouter([
    {
        path: "/",
        element: <App />
    }
])

const theme = createTheme({
    palette: {
        mode: "dark",
    }
})

const queryClient = new QueryClient();

createRoot(document.getElementById('root')).render(
    <ThemeProvider theme={theme}>
        <CssBaseline />
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    </ThemeProvider>
)
