package hcmut.edu.vn.tutor_support_system.service;

import hcmut.edu.vn.tutor_support_system.dto.MeetingNoteDto;
import hcmut.edu.vn.tutor_support_system.entity.MeetingNote;
import hcmut.edu.vn.tutor_support_system.entity.Session;
import hcmut.edu.vn.tutor_support_system.entity.User;
import hcmut.edu.vn.tutor_support_system.exception.ResourceNotFoundException;
import hcmut.edu.vn.tutor_support_system.mapper.DtoMapper;
import hcmut.edu.vn.tutor_support_system.repository.MeetingNoteRepository;
import hcmut.edu.vn.tutor_support_system.repository.SessionRepository;
import hcmut.edu.vn.tutor_support_system.util.ValidationUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingNoteService {

  private final MeetingNoteRepository meetingNoteRepository;
  private final SessionRepository sessionRepository;

  @Transactional(readOnly = true)
  public MeetingNoteDto getMeetingNoteById(String noteId) {
    ValidationUtil.validateNotEmpty(noteId, "Note ID");

    MeetingNote meetingNote =
        meetingNoteRepository
            .findByNoteId(noteId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Meeting note not found with id: " + noteId));
    return DtoMapper.toMeetingNoteDto(meetingNote);
  }

  @Transactional(readOnly = true)
  public List<MeetingNoteDto> getMeetingNotesBySessionId(String sessionId) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    List<MeetingNote> meetingNotes = meetingNoteRepository.findBySession(session);
    return meetingNotes.stream().map(DtoMapper::toMeetingNoteDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<MeetingNoteDto> getMeetingNotesByTutorId(String tutorId) {
    ValidationUtil.validateNotEmpty(tutorId, "Tutor ID");

    List<MeetingNote> meetingNotes = meetingNoteRepository.findByTutorId(tutorId);
    return meetingNotes.stream().map(DtoMapper::toMeetingNoteDto).collect(Collectors.toList());
  }

  @Transactional
  public MeetingNoteDto createMeetingNote(String sessionId, UUID createdById, String content) {
    ValidationUtil.validateNotEmpty(sessionId, "Session ID");
    ValidationUtil.validateNotNull(createdById, "Created by user ID");
    ValidationUtil.validateNotEmpty(content, "Note content");

    Session session =
        sessionRepository
            .findBySessionId(sessionId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId));

    MeetingNote meetingNote = new MeetingNote();
    meetingNote.setNoteId("MN-" + UUID.randomUUID().toString());
    meetingNote.setSession(session);
    User createdBy = new User();
    createdBy.setId(createdById);
    meetingNote.setCreatedBy(createdBy);
    meetingNote.setContent(content);

    MeetingNote savedNote = meetingNoteRepository.save(meetingNote);
    return DtoMapper.toMeetingNoteDto(savedNote);
  }

  @Transactional
  public MeetingNoteDto updateMeetingNote(String noteId, String content) {
    ValidationUtil.validateNotEmpty(noteId, "Note ID");
    ValidationUtil.validateNotEmpty(content, "Note content");

    MeetingNote meetingNote =
        meetingNoteRepository
            .findByNoteId(noteId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Meeting note not found with id: " + noteId));

    meetingNote.setContent(content);

    MeetingNote updatedNote = meetingNoteRepository.save(meetingNote);
    return DtoMapper.toMeetingNoteDto(updatedNote);
  }

  @Transactional
  public void deleteMeetingNote(String noteId) {
    ValidationUtil.validateNotEmpty(noteId, "Note ID");

    MeetingNote meetingNote =
        meetingNoteRepository
            .findByNoteId(noteId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Meeting note not found with id: " + noteId));

    meetingNoteRepository.delete(meetingNote);
  }

  @Transactional(readOnly = true)
  public List<MeetingNoteDto> getAllMeetingNotes() {
    List<MeetingNote> meetingNotes = meetingNoteRepository.findAll();
    return meetingNotes.stream().map(DtoMapper::toMeetingNoteDto).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Long getMeetingNoteCountByUserId(UUID userId) {
    ValidationUtil.validateNotNull(userId, "User ID");

    return meetingNoteRepository.countByCreatedById(userId);
  }
}
