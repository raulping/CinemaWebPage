package microblog.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import microblog.model.User;
import microblog.model.Movie;
import microblog.model.Projection;
import microblog.model.Reservation;
import microblog.model.Screen;

import microblog.model.UserRepository;
import microblog.model.MovieRepository;
import microblog.model.ProjectionRepository;
import microblog.model.ReservationRepository;
import microblog.model.ScreenRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import microblog.services.UserService;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import microblog.model.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping(path = "/")
public class MainController {

    /* REPOSITORIES */
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /* SERVICES */
    @Autowired
    private UserService userService;

    /* Home */
    @GetMapping(path = "/")
    public String mainView(Model model, Principal principal) {
        Iterable <Movie> movies;
        movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
        if (principal != null){
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
            Integer log = 1;
            model.addAttribute("log", log);
            return "Home";
        }else{
            User user = new User();
            user.setRole("USER");
            model.addAttribute("user", user);
            Integer nolog = 0;
            model.addAttribute("log", nolog);
            return "Home";
        }
    }

    /* Map */
    @GetMapping(path = "/map")
    public String mapView(Model model, Principal principal) {
        if (principal != null){
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
            Integer log = 1;
            model.addAttribute("log", log);
            return "Map";
        }else{
            User user = new User();
            user.setRole("USER");
            model.addAttribute("user", user);
            Integer nolog = 0;
            model.addAttribute("log", nolog);
            return "Map";
        }
    }


    /* Login */
    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }   
    
    /* Register user GET */
    @GetMapping(path = "/register")
    public String registerForm(@ModelAttribute("user") User user, Model model) {
        String role = "USER";
        model.addAttribute("role", role);
        return "register";
    }
    /* Register user POST */
    @PostMapping(path = "/register")
    public String register(@Valid @ModelAttribute("user") User user,
                        BindingResult bindingResult,
                        @RequestParam String passwordRepeat) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "redirect:register?duplicate_email";
        }
        if (userRepository.findByName(user.getName()) != null) {
            return "redirect:register?duplicate_name";
        }
        if (user.getPassword().equals(passwordRepeat)) {
            user.setRole("USER");
            userService.register(user);
        } else {
            return "redirect:register?passwords";
        }
        return "redirect:login?registered";
    }

    /* Register Movie GET */
    @GetMapping(path = "/register_movie")
    public String registerMovieForm(@ModelAttribute("movie") Movie movie) {
        return "register_movie";
    }   
    /* Register Movie POST */
    @PostMapping(path = "/register_movie")
    public String register_movie(@Valid @ModelAttribute Movie movie,
                                BindingResult bindingResult) {
        System.out.println("Register movie "+ bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            return "register_movie";
        }
        if (movieRepository.findByTitle(movie.getTitle()) != null) {
            return "redirect:register_movie?duplicate_movie";
        }
        movieRepository.save(movie);
        return "redirect:/";
    }
    /* Movie page */
    @GetMapping(path = "/movie/{movieId}")
    public String viewMovie(@PathVariable int movieId, Model model, Principal principal) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (!movie.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        model.addAttribute("movie", movie.get());
        if (principal != null){
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
            Integer log = 1;
            model.addAttribute("log", log);
            return "Movie";
        }
        User user = new User();
        user.setRole("USER");
        model.addAttribute("user", user);
        Integer nolog = 0;
        model.addAttribute("log", nolog);
        return "Movie";
    }
    /* Delete Movie */
    @PostMapping(path = "/delete_movie")
    public String deleteMovie(@RequestParam int movieId, Model model) {
        Optional<Movie> Optmovie = movieRepository.findById(movieId);
        if (!Optmovie.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        Movie movie = Optmovie.get();
        List<Projection> projections = movie.getProjections();
        for (int i = 0; i < projections.size(); i++) {
            Projection projection = projections.get(i);
            reservationRepository.deleteReservations(projection);
            projectionRepository.deleteById(projection.getId());
        }
        movieRepository.deleteById(movieId);
        return "redirect:/";
    }



    /* Register Projections GET */
    @GetMapping(path = "/projection_manager")
    public String registerProjectionForm(Model model, @ModelAttribute("projection") Projection projection) {
        Iterable <Movie> movies;
        movies = movieRepository.findAll();
        model.addAttribute("movies", movies);
       
        Iterable <Screen> screens;
        screens = screenRepository.findAll();
        model.addAttribute("screens", screens);
        
        return "register_projection";
    } 

    /* Register Projections POST */
    @PostMapping(path = "/projection_manager")
    public String register_projection(@Valid @RequestParam  Integer movieId, @RequestParam Integer screenId, @RequestParam String date, Model model) {
        System.out.println("Register projection ");
        if (movieRepository.findById(movieId) == null){
            System.out.println("title ");
            return "redirect:projection_manager?Movie Not Found";
        }else if (screenRepository.findById(screenId) == null){
            String error = "Screen Not Found";
            model.addAttribute(error);
        
        }else{
            Projection projection = new Projection();

            Movie movie = movieRepository.findById(movieId).get();
            System.out.println("Register projection "+ movie);
            projection.setMovie(movie);

            Screen screen = screenRepository.findById(screenId).get();
            System.out.println("Register projection "+ screen);
            projection.setScreen(screen);
            Integer seats = screen.getTotal_seats();
            System.out.println("Register projection "+ seats);
            projection.setAvailable_seats(seats);

            String esqueleton = "yyyy-MM-dd'T'HH:mm";
            SimpleDateFormat simpledateformat = new SimpleDateFormat(esqueleton);

            Date ProDate;
            try{
                ProDate = simpledateformat.parse(date);
            }catch (ParseException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INCORRECT DAY FORMAT");
            }
            projection.setDate(ProDate);
            

            projectionRepository.save(projection);

            model.addAttribute("Movie", movie);

            return "redirect:/movie/" + movie.getId();
        }
        return "redirect:/";
    }
    /* Projection page */
    @GetMapping(path = "/projection/{projectionId}")
    public String viewProjection(@PathVariable int projectionId, Model model) {
        Optional<Projection> Optprojection = projectionRepository.findById(projectionId);
        if (!Optprojection.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projection not found");
        }

        Projection projection = Optprojection.get();
        
        Integer reserved_seats = 0;
        if (projectionRepository.sumReservedSeats(projection) != null){
             reserved_seats = projectionRepository.sumReservedSeats(projection);
        }
        System.out.println("A" + reserved_seats);
        Integer total_seats = projection.getAvailable_seats();
        System.out.println("B" + total_seats);
        Integer available_seats = total_seats - reserved_seats;
        model.addAttribute("available_seats", available_seats);
        model.addAttribute("projection", projection);
        return "Projection";
    }

    /* Delete Projection */
    @PostMapping(path = "/delete_proj")
    public String deleteProjection(@RequestParam int projectionId, Model model) {
        Optional<Projection> Optprojection = projectionRepository.findById(projectionId);
        if (!Optprojection.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projection not found");
        }
        Projection projection = Optprojection.get();
        Movie movie = projection.getMovie();
        reservationRepository.deleteReservations(projection);
        projectionRepository.deleteById(projectionId);
        

        return "redirect:/movie/" + movie.getId();
    }


    /* Make reservation */ 
    @PostMapping(path = "/make_reservation")
    public String makeReservation(@Valid @RequestParam Integer tickets, Integer projectionId, Principal principal, Model model) {
        Reservation reservation = new Reservation();

        reservation.setSeats(tickets);
        
        Optional<Projection> Optprojection = projectionRepository.findById(projectionId);
        Projection projection = Optprojection.get();
        reservation.setProjection(projection);

        User user = userRepository.findByEmail(principal.getName());
        reservation.setUser(user);

    
        Date date = new Date();
        reservation.setDate(date);

        reservationRepository.save(reservation);
        return "redirect:/user" ;
    } 

    /* User page */
    @GetMapping(path = "/user")
    public String viewUser(Principal principal, Model model) {
        if (principal != null){
            User user = userRepository.findByEmail(principal.getName());
            model.addAttribute("user", user);
        }            
        return "user";
    }

    /* Screens GET */
    @GetMapping(path = "/register_screens")
    public String registerScreenForm(@ModelAttribute("screen") Screen screen, Model model) {
        List<Screen> screens = screenRepository.findAll();
        model.addAttribute("screens", screens);
        return "register_screen";
    }
    /* Screens POST */
    @PostMapping(path = "/edit_screen")
    public String register_screen(@Valid @ModelAttribute Screen screen,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register_screen";
        }
        if (screenRepository.findByName(screen.getName()) != null) {
            return "redirect:register_screens?duplicate_screen";
        }
        screenRepository.save(screen);
        return "redirect:/";
    }

    /* Delete Movie */
    @PostMapping(path = "/delete_screen")
    public String deleteScreen(@RequestParam int screenId, Model model) {
        Optional<Screen> Optscreen = screenRepository.findById(screenId);
        if (!Optscreen.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Screen not found");
        }
        Screen screen = Optscreen.get();
        List<Projection> projections = screen.getProjections();
        for (int i = 0; i < projections.size(); i++){
            Projection projection = projections.get(i);
            reservationRepository.deleteReservations(projection);
            projectionRepository.deleteById(projection.getId());
        }
        screenRepository.deleteById(screenId);
        return "redirect:/register_screens";
    }
}
