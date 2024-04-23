package com.example.myproject.views.reviewview;

import com.example.myproject.data.Review;
import com.example.myproject.data.SampleBook;
import com.example.myproject.data.SampleBookRepository;
import com.example.myproject.services.ReviewService;
import com.example.myproject.data.ReviewRepo;
import com.example.myproject.services.SampleBookService;
import com.example.myproject.views.MainLayout;
import com.example.myproject.views.products.ProductsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Base64;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Review View")
@Route(value = "review-view/:reviewID?/:action?(edit)", layout = MainLayout.class)
public class ReviewViewView extends Div implements BeforeEnterObserver {

    private final String Review_ID = "reviewID";
    private final String Review_EDIT_ROUTE_TEMPLATE = "review-view/%s/edit";

    private final Grid<Review> grid = new Grid<>(Review.class, false);

    //private final SampleBookRepository sampleBookRepository;
    //private final ReviewRepo reviewRepo;
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");

    private final BeanValidationBinder<Review> binder;

    private Review review;

    private final ReviewService reviewService;

    private TextField title;
    private TextField comment;

    public ReviewViewView(ReviewService reviewService) {
        this.reviewService = reviewService;
        addClassNames("review-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("title").setAutoWidth(true);
        grid.addColumn("comment").setAutoWidth(true);
        grid.setItems(query -> reviewService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(Review_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ProductsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Review.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.forField(pages).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("pages");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        delete.addClickListener(e -> {
            if (this.review != null) {
                reviewService.delete(this.review.getId());
                refreshGrid();
            } else {
                Notification.show("No review selected for deletion", 3000, Notification.Position.MIDDLE);
            }
        });

        save.addClickListener(e -> {
            try {
                if (this.review == null) {
                    this.review = new Review();
                }
                binder.writeBean(this.review);
                reviewService.update(this.review);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ProductsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> reviewId = event.getRouteParameters().get(Review_ID).map(Long::parseLong);
        if (reviewId.isPresent()) {
            Optional<Review> reviewFromBackend = reviewService.get(reviewId.get());
            if (reviewFromBackend.isPresent()) {
                populateForm(reviewFromBackend.get());
            } else {
                Notification.show(String.format("The requested sampleBook was not found, ID = %s", reviewId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProductsView.class);
            }
        }
    }
    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        title = new TextField("Title");
        //author = new TextField("Author");
        //pages = new TextField("Pages");
        comment=new TextField("Comment");
        formLayout.add(title, comment);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel,delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Review value) {
        this.review = value;
        binder.readBean(this.review);
    }
}