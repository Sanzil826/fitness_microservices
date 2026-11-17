
import {
  Box,
  Typography,
  Card,
  CardContent,
  Divider,
} from "@mui/material";

import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { getActivityDetail } from "../services/api";

const ActivityDetail = () => {
  const { id } = useParams();

  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await getActivityDetail(id);

        setActivity(response.data);
        setRecommendation(response.data);
      } catch (error) {
        console.error("Failed to fetch activity details:", error);
        setError("Unable to load activity details. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchActivityDetail();
    }
  }, [id]);

  if (loading) {
    return <Typography>Loading activity details...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  if (!activity) {
    return <Typography>Activity not found.</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>

      {/* Activity Details */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Activity Details
          </Typography>

          <Typography>Activity Type: {activity.activityType}</Typography>
          <Typography>Activity ID: {activity.activityId}</Typography>
          <Typography>Duration: {activity.duration ?? "Not available"} minutes</Typography>
          <Typography>
            Calories Burned: {activity.calorieBurned ?? "Not available"}
          </Typography>
          <Typography>
            Date: {activity.createdAt
              ? new Date(activity.createdAt).toLocaleString()
              : "Not available"}
          </Typography>
        </CardContent>
      </Card>

      {/* AI Recommendation */}
      {recommendation && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              AI Recommendation
            </Typography>

            {/* Overall Analysis */}
            <Typography variant="h6">Overall Analysis</Typography>

            <Typography sx={{ mb: 2, whiteSpace: "pre-line" }}>
              {recommendation.recommendation || "No analysis available."}
            </Typography>

            <Divider sx={{ my: 2 }} />

            {/* Improvements */}
            <Typography variant="h6">Improvements</Typography>

            {recommendation.improvements?.map((improvement, index) => (
              <Typography key={index} sx={{ mb: 1 }}>
                • {improvement}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            {/* Suggestions */}
            <Typography variant="h6">Suggestions</Typography>

            {recommendation.suggestions?.map((suggestion, index) => (
              <Typography key={index} sx={{ mb: 1 }}>
                • {suggestion}
              </Typography>
            ))}

            <Divider sx={{ my: 2 }} />

            {/* Safety Guidelines */}
            <Typography variant="h6">Safety Guidelines</Typography>

            {recommendation.safety?.map((safety, index) => (
              <Typography key={index} sx={{ mb: 1 }}>
                • {safety}
              </Typography>
            ))}
          </CardContent>
        </Card>
      )}

    </Box>
  );
};

export default ActivityDetail;
