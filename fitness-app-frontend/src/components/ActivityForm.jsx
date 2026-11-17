import { Box, duration,InputLabel ,MenuItem,Button, FormControl, Select, TextField } from "@mui/material";
import { useState } from "react";
import { addActivities } from "../services/api";



const ActivityForm = ({ onActivityAdded }) => {

    const[activity, setActivity]=useState({
        type: "RUNNING",
         duration:'',
         calorieBurned:'',
        additionalMetrics:{}
    })

const handleSubmit = async(e)=>{
    e.preventDefault();
    try{
        await addActivities(activity);
        onActivityAdded();
        setActivity({type: "RUNNING",duration:'', caloriesBurned:''});
    }catch(error){
        console.error(error);
    }
}
    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mb: 4}}>
            <FormControl fullWidth sx={{mb: 2}}>
                <InputLabel>Activity Type</InputLabel>
                <Select
                    value={activity.type}
                    onChange={(e)=> setActivity({...activity, type: e.target.value})}>
                        <MenuItem value="RUNNING">Running</MenuItem>
                        <MenuItem value="WALKING">WALKING</MenuItem>
                        <MenuItem value="CYCLING">CYCLING</MenuItem>
                    </Select>
            </FormControl>
            
            <TextField fullWidth
                        label="Duration(Minutes)"
                        type="number"
                        sx={{mb:2}}
                        value={activity.duration}
                        onChange={(e)=> setActivity({...activity, duration: e.target.value})}/>
            
            <TextField fullWidth
                        label="Calories Burned"
                        type="number"
                        sx={{mb:2}}
                        value={activity.calorieBurned}
                        onChange={(e)=> setActivity({...activity, calorieBurned: e.target.value})}/>
            
            <Button type='submit' variant='contained'>
                Add Activity
            </Button>
    </Box>
    );
};

export default ActivityForm;