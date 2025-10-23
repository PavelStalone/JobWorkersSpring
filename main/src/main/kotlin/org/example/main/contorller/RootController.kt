package org.example.main.contorller

import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RootController {

    @GetMapping
    fun getRoot(): RepresentationModel<*> {
        val rootModel = RepresentationModel<Nothing>()

        rootModel.add(
            Link.of("/swagger-ui.html", "documentation"),
            linkTo(methodOn(WorkerController::class.java).getAllWorkers(0, 10)).withRel("workers"),
            linkTo(methodOn(JobController::class.java).getAllJobs(0, 10)).withRel("jobs"),
        )

        return rootModel
    }
}
