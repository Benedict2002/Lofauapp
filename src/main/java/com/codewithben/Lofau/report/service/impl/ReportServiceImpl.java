package com.codewithben.Lofau.report.service.impl;

import com.codewithben.Lofau.Post.repository.PostRepository;
import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.User.userRepo.UserRepository;
import com.codewithben.Lofau.comment.repository.CommentRepository;
import com.codewithben.Lofau.group.repository.GroupRepository;
import com.codewithben.Lofau.report.dto.request.CreateReportRequest;
import com.codewithben.Lofau.report.dto.response.ReportResponse;
import com.codewithben.Lofau.report.entity.Report;
import com.codewithben.Lofau.report.mapper.ReportMapper;
import com.codewithben.Lofau.report.repository.ReportRepository;
import com.codewithben.Lofau.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final UserRepository userRepository;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;

    @Override
    public ReportResponse createReport(CreateReportRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User reporter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean alreadyReported =
                reportRepository.existsByReporterAndTargetIdAndTargetType(
                        reporter,
                        request.getTargetId(),
                        request.getTargetType()
                );

        if (alreadyReported) {
            throw new RuntimeException(
                    "You have already reported this item."
            );
        }

        switch (request.getTargetType()) {

            case POST -> postRepository.findById(request.getTargetId())
                    .orElseThrow(() ->
                            new RuntimeException("Post not found"));

            case COMMENT -> commentRepository.findById(request.getTargetId())
                    .orElseThrow(() ->
                            new RuntimeException("Comment not found"));

            case GROUP -> groupRepository.findById(request.getTargetId())
                    .orElseThrow(() ->
                            new RuntimeException("Group not found"));
        }

        Report report = reportMapper.toEntity(request);

        report.setReporter(reporter);

        report = reportRepository.save(report);

        return reportMapper.toResponse(report);
    }

}