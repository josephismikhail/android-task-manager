package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.List;



import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final TaskRepository taskRepository;


    // UI state
    private final SimpleSubject<List<Integer>> cardOrdering;
    private final SimpleSubject<List<Task>> orderedCards;

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

            var ordering = new ArrayList<Integer>();
            for (int i = 0; i < cards.size(); i++) {
                ordering.add(i);
            }
            cardOrdering.setValue(ordering);
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

    public SimpleSubject<List<Task>> getOrderedCards() {
        return orderedCards;
    }
}
