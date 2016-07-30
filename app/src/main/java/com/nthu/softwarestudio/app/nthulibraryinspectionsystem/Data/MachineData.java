package com.nthu.softwarestudio.app.nthulibraryinspectionsystem.Data;

/**
 * Created by Ywuan on 29/07/2016.
 */
public class MachineData {
    private String machine_id;
    private String place;
    private String date;
    private String user_name;
    private String state;
    private String problem;
    private String solution;
    private String solve_date;
    private String branch;
    private String floor;

    public MachineData(String machine_id, String place, String date, String user_name, String state, String problem, String solution, String solve_date, String branch, String floor) {
        this.machine_id = machine_id;
        this.place = place;
        this.date = date;
        this.user_name = user_name;
        this.state = state;
        this.problem = problem;
        this.solution = solution;
        this.solve_date = solve_date;
        this.branch = branch;
        this.floor = floor;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolve_date() {
        return solve_date;
    }

    public void setSolve_date(String solve_date) {
        this.solve_date = solve_date;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
