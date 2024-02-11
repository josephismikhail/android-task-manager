package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final TaskRepository taskRepository;

    // UI state
    private final MutableSubject<List<Integer>> cardOrdering;
    private final MutableSubject<List<Task>> orderedCards;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        // Create the observable subjects.
        this.cardOrdering = new SimpleSubject<>();
        this.orderedCards = new SimpleSubject<>();

        // When the list of cards changes (or is first loaded), reset the ordering.
        taskRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Task::id))
                    .collect(Collectors.toList());
            orderedCards.setValue(newOrderedCards);
        });

        cardOrdering.observe(ordering -> {
            if (ordering == null) return;

            var cards = new ArrayList<Task>();
            for (var id : ordering) {
                var card = taskRepository.find(id).getValue();
                if (card == null) return;
                cards.add(card);
            }
            this.orderedCards.setValue(cards);
        });
    }

    public Subject<List<Task>> getOrderedCards() {
        return orderedCards;
    }
}
