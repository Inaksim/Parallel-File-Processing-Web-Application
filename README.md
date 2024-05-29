# Parallel-File-Processing-Web-Application
Parallel File Processing Web Application

Explanation
ExecutorService: The ExecutorService is used to manage a pool of threads. The newFixedThreadPool(10) method creates a thread pool with 10 threads, allowing up to 10 files to be processed concurrently.
Processing Files in Parallel: For each uploaded file, a Callable task is submitted to the ExecutorService. This task processes the file (in this case, extracting metadata using Apache Tika).
Handling Results: The results of the parallel tasks are collected using Future objects. The future.get() method waits for each task to complete and retrieves the result.
Displaying Results: The results are then added to the model and displayed in the Thymeleaf template.
This implementation showcases how to use Java's concurrency utilities for parallel processing within a web application context.
