# RCPSP-Solver
Different heuristics in order to solve the RCPSP problem

The goal of this project is to create different heuristics for the RCPSP problem and compare the results for different sets of data.
The most simple heuristics are choosing which tasks to complete based on a priority list. 
The priority list is the list of all the tasks that can be processed at the current time, meaning that there is enough ressources
and that all the predecessors tasks are completed.
Latest and Earliest starting times are calculated and a few heuristics are based on these factors. 
More complex heuristics, such as WCS have also been implemented. 
See --> http://www.om-db.wi.tum.de/psplib/files/handbook1.pdf
