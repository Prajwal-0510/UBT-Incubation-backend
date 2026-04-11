package com.ubt.backend.config;

import com.ubt.backend.entity.GalleryItem;
import com.ubt.backend.entity.Project;
import com.ubt.backend.entity.Testimonial;
import com.ubt.backend.entity.Update;
import com.ubt.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds the database with initial data from AdminContext.jsx seed values.
 * Only runs when the tables are empty (safe for production restart).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired private GalleryRepository   galleryRepository;
    @Autowired private ProjectRepository   projectRepository;
    @Autowired private UpdateRepository    updateRepository;
    @Autowired private TestimonialRepository testimonialRepository;

    @Override
    public void run(String... args) {
        seedGallery();
        seedProjects();
        seedUpdates();
        seedTestimonials();
    }

    // ─── GALLERY ──────────────────────────────────────────────────────────────

    private void seedGallery() {
        if (galleryRepository.count() > 0) return;
        log.info("Seeding gallery...");

        galleryRepository.save(GalleryItem.builder()
                .title("IIT Nagpur Campus Visit")
                .category("college-visit")
                .imgUrl("https://images.unsplash.com/photo-1562774053-701939374585?w=600")
                .date(LocalDate.of(2024, 3, 15)).build());

        galleryRepository.save(GalleryItem.builder()
                .title("VNIT Lab Tour")
                .category("college-visit")
                .imgUrl("https://images.unsplash.com/photo-1523240795612-9a054b0db644?w=600")
                .date(LocalDate.of(2024, 2, 20)).build());

        galleryRepository.save(GalleryItem.builder()
                .title("Symbiosis University Workshop")
                .category("college-visit")
                .imgUrl("https://images.unsplash.com/photo-1509062522246-3755977927d7?w=600")
                .date(LocalDate.of(2024, 1, 10)).build());

        galleryRepository.save(GalleryItem.builder()
                .title("AI Attendance System")
                .category("student-project")
                .imgUrl("https://images.unsplash.com/photo-1531482615713-2afd69097998?w=600")
                .date(LocalDate.of(2024, 3, 1)).build());

        galleryRepository.save(GalleryItem.builder()
                .title("Smart Traffic Controller")
                .category("student-project")
                .imgUrl("https://images.unsplash.com/photo-1581092921461-eab62e97a780?w=600")
                .date(LocalDate.of(2024, 2, 15)).build());

        galleryRepository.save(GalleryItem.builder()
                .title("NLP Research Dashboard")
                .category("student-project")
                .imgUrl("https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=600")
                .date(LocalDate.of(2024, 1, 22)).build());

        log.info("Gallery seeded with 6 items.");
    }

    // ─── PROJECTS ─────────────────────────────────────────────────────────────

    private void seedProjects() {
        if (projectRepository.count() > 0) return;
        log.info("Seeding projects...");

        projectRepository.save(Project.builder()
                .title("Smart Parking System using IoT")
                .domain("IoT / Embedded")
                .level("BE/BTech")
                .imgUrl("https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=600")
                .description("An IoT-based automated parking management system using RFID and ultrasonic sensors.")
                .techStack("Arduino,RFID,NodeMCU,Firebase")
                .duration("4 months")
                .date("2024-03").build());

        projectRepository.save(Project.builder()
                .title("Deep Learning for Medical Image Analysis")
                .domain("AI & ML")
                .level("PhD R&D")
                .imgUrl("https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=600")
                .description("A CNN-based deep learning model achieving 97.3% accuracy in detecting tumor regions from MRI scans.")
                .techStack("Python,TensorFlow,OpenCV,DICOM")
                .duration("18 months")
                .date("2024-02").build());

        projectRepository.save(Project.builder()
                .title("Blockchain-based Supply Chain Tracker")
                .domain("Blockchain")
                .level("ME/MTech")
                .imgUrl("https://images.unsplash.com/photo-1639322537228-f710d846310a?w=600")
                .description("Decentralized supply chain transparency platform built on Ethereum smart contracts.")
                .techStack("Solidity,React,Web3.js,IPFS")
                .duration("8 months")
                .date("2024-01").build());

        projectRepository.save(Project.builder()
                .title("NLP-based Resume Screening Tool")
                .domain("NLP / AI")
                .level("MBA Projects")
                .imgUrl("https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600")
                .description("An intelligent HR tool using BERT and transformer models to automate resume screening.")
                .techStack("Python,BERT,FastAPI,PostgreSQL")
                .duration("6 months")
                .date("2023-12").build());

        log.info("Projects seeded with 4 items.");
    }

    // ─── UPDATES ──────────────────────────────────────────────────────────────

    private void seedUpdates() {
        if (updateRepository.count() > 0) return;
        log.info("Seeding updates...");

        updateRepository.save(Update.builder()
                .type("announcement")
                .title("New Batch for PhD Assistance Program — April 2025")
                .content("Enroll now for our upcoming PhD guidance batch starting April 15. Limited seats available. Contact us today!")
                .date(LocalDate.of(2025, 3, 28))
                .pinned(true).build());

        updateRepository.save(Update.builder()
                .type("achievement")
                .title("50 Scopus Publications Milestone Reached!")
                .content("UBT TECHNOLOGY is proud to announce 50 successful Scopus-indexed publications in Q1 2025 alone.")
                .date(LocalDate.of(2025, 3, 20))
                .pinned(false).build());

        updateRepository.save(Update.builder()
                .type("event")
                .title("Free Webinar: How to Write a Scopus Paper")
                .content("Join our free webinar on April 5th, 2025 covering structure, methodology, and submission strategies.")
                .date(LocalDate.of(2025, 3, 15))
                .pinned(false).build());

        updateRepository.save(Update.builder()
                .type("news")
                .title("UBT TECHNOLOGY Opens Ahilyanagar Branch Office")
                .content("We are excited to announce our new branch in Ahilyanagar, Maharashtra. Walk-ins welcome from April 1, 2025.")
                .date(LocalDate.of(2025, 3, 10))
                .pinned(false).build());

        log.info("Updates seeded with 4 items.");
    }

    // ─── TESTIMONIALS ─────────────────────────────────────────────────────────

    private void seedTestimonials() {
        if (testimonialRepository.count() > 0) return;
        log.info("Seeding testimonials...");

        testimonialRepository.save(Testimonial.builder()
                .name("Dr. Priya Sharma")
                .role("Assistant Professor, VNIT Nagpur")
                .text("UBT TECHNOLOGY helped me publish 3 Scopus papers during my PhD. Their guidance was exceptional.")
                .rating(5)
                .avatar("https://i.pravatar.cc/60?img=5").build());

        testimonialRepository.save(Testimonial.builder()
                .name("Rahul Mishra")
                .role("MTech Student, IIT Bombay")
                .text("Got my final project published in an IEEE conference. The support team was responsive and extremely professional.")
                .rating(5)
                .avatar("https://i.pravatar.cc/60?img=3").build());

        testimonialRepository.save(Testimonial.builder()
                .name("Dr. Anjali Patel")
                .role("Research Scholar, Pune University")
                .text("From patent filing to journal publication — UBT TECHNOLOGY handled everything seamlessly. Highly recommended!")
                .rating(5)
                .avatar("https://i.pravatar.cc/60?img=9").build());

        testimonialRepository.save(Testimonial.builder()
                .name("Saurabh Desai")
                .role("BE Final Year, COEP Pune")
                .text("My final year project got selected at an international conference. The team guided me at every step — amazing experience!")
                .rating(5)
                .avatar("https://i.pravatar.cc/60?img=12").build());

        log.info("Testimonials seeded with 4 items.");
    }
}
