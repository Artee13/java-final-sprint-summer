package com.gym.dao;

import java.util.List;

public interface WorkoutClassDAO {
    class WorkoutClass {
        public int id;
        public String type;
        public String description;
        public int trainerId;

        public WorkoutClass(int id, String type, String description, int trainerId) {
            this.id = id;
            this.type = type;
            this.description = description;
            this.trainerId = trainerId;
        }

        @Override
        public String toString() {
            return type + " - " + description + " (Trainer ID: " + trainerId + ")";
        }
    }

    List<WorkoutClass> findAll();
    List<WorkoutClass> findByTrainer(int trainerId);
    WorkoutClass addClass(WorkoutClass c);
}