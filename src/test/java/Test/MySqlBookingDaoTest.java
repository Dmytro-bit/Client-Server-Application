package Test;

import org.example.DTOs.Booking;
import org.example.Utils.BookingStatus;
import org.example.server.DAOs.MySqlBookingDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class MySqlBookingDaoTest {

    private MySqlBookingDao bookingDao;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResult;


    @org.junit.jupiter.api.BeforeEach
    void setUp() throws SQLException {

        bookingDao = Mockito.spy(new MySqlBookingDao());

        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResult = mock(ResultSet.class);

        doReturn(mockConnection).when(bookingDao).getConnection();
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResult);

    }

    @org.junit.jupiter.api.Test
    void getAllEntities() throws SQLException {

        when(mockResult.next()).thenReturn(true, false); // Only one row
        when(mockResult.getInt("id")).thenReturn(1);
        when(mockResult.getInt("customer_id")).thenReturn(100);
        when(mockResult.getInt("table_id")).thenReturn(5);
        when(mockResult.getDate("booking_date")).thenReturn(Date.valueOf("2025-04-03"));
        when(mockResult.getTime("start_time")).thenReturn(Time.valueOf("12:00:00"));
        when(mockResult.getTime("end_time")).thenReturn(Time.valueOf("14:00:00"));
        when(mockResult.getString("status")).thenReturn("CONFIRMED");

        List<Booking> result = bookingDao.getAllEntities();

        assertEquals(1, result.size());

        Booking booking = result.get(0);
        assertEquals(1, booking.getId());
        assertEquals(100, booking.getCustomer_id());
        assertEquals(5, booking.getTable_id());
        assertEquals(Date.valueOf("2025-04-03"), booking.getBookingDate());
        assertEquals(Time.valueOf("12:00:00"), booking.getStartTime());
        assertEquals(Time.valueOf("14:00:00"), booking.getEndTime());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void getEntityByID() throws SQLException {
        int searchId = 1;

        when(mockResult.next()).thenReturn(true, false);
        when(mockResult.getInt("id")).thenReturn(searchId);
        when(mockResult.getInt("customer_id")).thenReturn(100);
        when(mockResult.getInt("table_id")).thenReturn(5);
        when(mockResult.getDate("booking_date")).thenReturn(Date.valueOf("2025-04-03"));
        when(mockResult.getTime("start_time")).thenReturn(Time.valueOf("12:00:00"));
        when(mockResult.getTime("end_time")).thenReturn(Time.valueOf("14:00:00"));
        when(mockResult.getString("status")).thenReturn("CONFIRMED");

        Booking booking = bookingDao.getEntityById(searchId);

        assertNotNull(booking);
        assertEquals(1, booking.getId());
        assertEquals(100, booking.getCustomer_id());
        assertEquals(5, booking.getTable_id());
        assertEquals(Date.valueOf("2025-04-03"), booking.getBookingDate());
        assertEquals(Time.valueOf("12:00:00"), booking.getStartTime());
        assertEquals(Time.valueOf("14:00:00"), booking.getEndTime());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void insertEntity() throws SQLException {
        Booking test = new Booking(101, 101, 101, Date.valueOf("2025-04-03"),
                Time.valueOf("12:00:00"), Time.valueOf("14:00:00"), BookingStatus.CONFIRMED);

        doNothing().when(bookingDao).insertEntity(any(Booking.class));

        when(mockResult.next()).thenReturn(true, false);
        when(mockResult.getInt("id")).thenReturn(101);
        when(mockResult.getInt("customer_id")).thenReturn(101);
        when(mockResult.getInt("table_id")).thenReturn(101);
        when(mockResult.getDate("booking_date")).thenReturn(Date.valueOf("2025-04-03"));
        when(mockResult.getTime("start_time")).thenReturn(Time.valueOf("12:00:00"));
        when(mockResult.getTime("end_time")).thenReturn(Time.valueOf("14:00:00"));
        when(mockResult.getString("status")).thenReturn("CONFIRMED");

        bookingDao.insertEntity(test);

        Booking booking = bookingDao.getEntityById(101);

        assertNotNull(booking);
        assertEquals(101, booking.getId());
        assertEquals(101, booking.getCustomer_id());
        assertEquals(101, booking.getTable_id());
        assertEquals(Date.valueOf("2025-04-03"), booking.getBookingDate());
        assertEquals(Time.valueOf("12:00:00"), booking.getStartTime());
        assertEquals(Time.valueOf("14:00:00"), booking.getEndTime());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}