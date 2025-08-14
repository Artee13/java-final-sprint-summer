package com.gym.services;

import com.gym.dao.WorkoutClassDAO;
import com.gym.dao.WorkoutClassDAO.WorkoutClass;

import java.util.List;

public class WorkoutClassService {
    private final WorkoutClassDAO dao;

    public WorkoutClassService(WorkoutClassDAO dao) {
        this.dao = dao;
    }

    public List<WorkoutClass> allClasses() {
        return dao.findAll();
    }

    public List<WorkoutClass> classesByTrainer(int trainerId) {
        return dao.findByTrainer(trainerId);
    }

    public WorkoutClass add(String type, String desc, int trainerId) {
        return dao.addClass(new WorkoutClass(0, type, desc, trainerId));
    }
}