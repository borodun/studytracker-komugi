import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        user: null,
        token: null,
        activities: [],
        errorMessage: '',
    },
    getters: {
        user: (state) => {
            return state.user;
        },
        token: (state) => {
            return state.token;
        },
        getActivities(state) {
            const activities = JSON.parse(localStorage.getItem('activities'));
            if (activities && activities.length > 0) {
                state.activities = activities;
            }

            return state.activities;
        },

        getErrorMessage(state) {
            return state.errorMessage;
        },
    },
    actions: {
        user(context, user) {
            context.commit("user", user)
        },
        token(context, token) {
            context.commit("token", token)
        },
        addActivity({ commit }, { activity }) {
            commit('addActivity', activity);
        },
        deleteActivity({ commit }, { activity }) {
            commit('deleteActivity', activity);
        },
        changeActivityState({ commit }, { activity }) {
            commit('changeActivityState', activity);
        },
        clearActivities({ commit }){
            commit('clearActivities');
        },
        setErrorMessageAction({ commit }, { message }) {
            commit('setErrorMessage', message);
        },
    },
    mutations: {
        user(state, user) {
            state.user = user;
        },
        token(state, token) {
            state.token = token;
        },
        addActivity(state, activity) {
            state.activities.push(activity);
            localStorage.setItem('activities', JSON.stringify(state.activities));
        },

        deleteActivity(state, activity) {
            state.activities = state.activities.filter((val) => val.id !== activity.id);
            localStorage.setItem('activities', JSON.stringify(state.activities));
        },

        changeActivityState(state, activity) {
            state.activities.map((val) => {
                if (val.id === activity.id) {
                    val.completed = !val.completed;
                }

                return val;
            });

            localStorage.setItem('activities', JSON.stringify(state.activities));
        },

        clearActivities(state) {
            state.activities = []
        },

        setErrorMessage(state, message) {
            state.errorMessage = message;
        },
    },
})

export default store